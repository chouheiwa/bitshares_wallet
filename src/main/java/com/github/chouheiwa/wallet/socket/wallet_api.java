package com.github.chouheiwa.wallet.socket;

import com.github.chouheiwa.wallet.utils.CalculateUtils;
import com.github.chouheiwa.wallet.utils.TextUtils;
import com.github.chouheiwa.wallet.socket.chain.*;
import com.github.chouheiwa.wallet.socket.common.ErrorCode;
import com.github.chouheiwa.wallet.socket.exception.NetworkStatusException;
import com.github.chouheiwa.wallet.socket.fc.crypto.aes;
import com.github.chouheiwa.wallet.socket.fc.crypto.sha256_object;
import com.github.chouheiwa.wallet.socket.fc.crypto.sha512_object;
import com.github.chouheiwa.wallet.socket.fc.io.base_encoder;
import com.github.chouheiwa.wallet.socket.fc.io.datastream_encoder;
import com.github.chouheiwa.wallet.socket.fc.io.datastream_size_encoder;
import com.github.chouheiwa.wallet.socket.fc.io.raw_type;
import com.github.chouheiwa.wallet.socket.market.MarketTicker;
import com.github.chouheiwa.wallet.socket.market.OrderBook;
import com.google.common.primitives.UnsignedInteger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.github.chouheiwa.wallet.utils.NumberUtils;
import com.github.chouheiwa.wallet.net.model.AllHistory;
import com.github.chouheiwa.wallet.net.model.HistoryResponseModel;
import org.bitcoinj.core.ECKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.digests.SHA512Digest;

import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class wallet_api {
    protected  final Logger log = LoggerFactory.getLogger(this.getClass());

    class wallet_object {
        sha256_object chain_id;
        List<account_object> my_accounts = new ArrayList<>();
        ByteBuffer cipher_keys;
        HashMap<object_id<account_object>, List<types.public_key_type>> extra_keys = new HashMap<>();

        public void update_account(account_object accountObject) {
            boolean bUpdated = false;
            for (int i = 0; i < my_accounts.size(); ++i) {
                if (my_accounts.get(i).id == accountObject.id) {
                    my_accounts.remove(i);
                    my_accounts.add(accountObject);
                    bUpdated = true;
                    break;
                }
            }

            if (bUpdated == false) {
                my_accounts.add(accountObject);
            }
        }
    }

    private websocket_api mWebsocketApi = new websocket_api();
    private wallet_object mWalletObject;
    private boolean mbLogin = false;
    private HashMap<types.public_key_type, types.private_key_type> mHashMapPub2Priv = new HashMap<>();
    private sha512_object mCheckSum = new sha512_object();
    private String ws_server;
    static class plain_keys {
        Map<types.public_key_type, String> keys;
        sha512_object checksum;

        public void write_to_encoder(base_encoder encoder) {
            raw_type rawType = new raw_type();

            rawType.pack(encoder, UnsignedInteger.fromIntBits(keys.size()));
            for (Map.Entry<types.public_key_type, String> entry : keys.entrySet()) {
                encoder.write(entry.getKey().key_data);

                byte[] byteValue = entry.getValue().getBytes();
                rawType.pack(encoder, UnsignedInteger.fromIntBits(byteValue.length));
                encoder.write(byteValue);
            }
            encoder.write(checksum.hash);
        }

        public static plain_keys from_input_stream(InputStream inputStream) {
            plain_keys keysResult = new plain_keys();
            keysResult.keys = new HashMap<>();
            keysResult.checksum = new sha512_object();

            raw_type rawType = new raw_type();
            UnsignedInteger size = rawType.unpack(inputStream);
            try {
                for (int i = 0; i < size.longValue(); ++i) {
                    types.public_key_type publicKeyType = new types.public_key_type();
                    inputStream.read(publicKeyType.key_data);

                    UnsignedInteger strSize = rawType.unpack(inputStream);
                    byte[] byteBuffer = new byte[strSize.intValue()];
                    inputStream.read(byteBuffer);
                    String strPrivateKey = new String(byteBuffer);
                    keysResult.keys.put(publicKeyType, strPrivateKey);
                }
                inputStream.read(keysResult.checksum.hash);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return keysResult;
        }
    }

    public wallet_api(String ws_server) {
        this.ws_server = ws_server;
    }

    public int initialize(String chain_id) {
        int nRet = mWebsocketApi.connect(ws_server);
        if (nRet == 0) {
            sha256_object sha256Object = sha256_object.create_from_chain_id(chain_id);

            if (mWalletObject == null) {
                mWalletObject = new wallet_object();
                mWalletObject.chain_id = sha256Object;
            } else if (mWalletObject.chain_id != null && mWalletObject.chain_id.equals(sha256Object) == false) {
                nRet = -1;
            }
        }
        return nRet;
    }

    public int reset() {
        //for java wallet reset
        mWebsocketApi.close();
        mWalletObject = null;
        mbLogin = false;
        mHashMapPub2Priv.clear();
        mCheckSum = new sha512_object();
        return 0;
    }

    public void close() {
        mWebsocketApi.close();
    }

    public boolean is_locked() {
        if (mWalletObject.cipher_keys.array().length > 0 &&
                mCheckSum.equals(new sha512_object())) {
            return true;
        }
        return false;
    }

    private void encrypt_keys() {
        plain_keys data = new plain_keys();
        data.keys = new HashMap<>();
        for (Map.Entry<types.public_key_type, types.private_key_type> entry : mHashMapPub2Priv.entrySet()) {
            data.keys.put(entry.getKey(), entry.getValue().toString());
        }
        data.checksum = mCheckSum;

        datastream_size_encoder sizeEncoder = new datastream_size_encoder();
        data.write_to_encoder(sizeEncoder);
        datastream_encoder encoder = new datastream_encoder(sizeEncoder.getSize());
        data.write_to_encoder(encoder);

        byte[] byteKey = new byte[32];
        System.arraycopy(mCheckSum.hash, 0, byteKey, 0, byteKey.length);
        byte[] ivBytes = new byte[16];
        System.arraycopy(mCheckSum.hash, 32, ivBytes, 0, ivBytes.length);

        mWalletObject.cipher_keys = aes.encrypt(byteKey, ivBytes, encoder.getData());

        return;
    }

    public int lock() {
        encrypt_keys();
        mCheckSum = new sha512_object();
        mHashMapPub2Priv.clear();
        return 0;
    }

    public int unlock(String strPassword) {
        assert (strPassword.length() > 0);

        //for java wallet unlok
        sha512_object passwordHash = sha512_object.create_from_string(strPassword);
        if (passwordHash == null || passwordHash.hash == null) {
            return -1;
        }
        byte[] byteKey = new byte[32];
        System.arraycopy(passwordHash.hash, 0, byteKey, 0, byteKey.length);
        byte[] ivBytes = new byte[16];
        System.arraycopy(passwordHash.hash, 32, ivBytes, 0, ivBytes.length);
        if (mWalletObject == null || mWalletObject.cipher_keys == null) {
            return -1;
        }

        ByteBuffer byteDecrypt = aes.decrypt(byteKey, ivBytes, mWalletObject.cipher_keys.array());
        if (byteDecrypt == null || byteDecrypt.array().length == 0) {
            return -1;
        }

        plain_keys dataResult = plain_keys.from_input_stream(
                new ByteArrayInputStream(byteDecrypt.array())
        );

        for (Map.Entry<types.public_key_type, String> entry : dataResult.keys.entrySet()) {
            types.private_key_type privateKeyType = new types.private_key_type(entry.getValue());
            if (!TextUtils.isEmpty(privateKeyType.toString()))
                mHashMapPub2Priv.put(entry.getKey(), privateKeyType);
        }

        mCheckSum = passwordHash;
        if (passwordHash.equals(dataResult.checksum)) {
            return 0;
        } else {
            return -1;
        }
    }

    public boolean is_new() {
        if (mWalletObject == null || mWalletObject.cipher_keys == null) {
            return true;
        }

        return (mWalletObject.cipher_keys.array().length == 0 &&
                mCheckSum.equals(new sha512_object()));
    }

    //根据账户名称查询账户私钥
    public String getWifKeyWithAccount(String accountName) {
        String result = "";
        for (int i = 0; i < list_my_accounts().size(); i++) {
            account_object accountObject = list_my_accounts().get(i);
            String account_name = accountObject.name.toString();
            if (account_name.equals(accountName)) {
                types.public_key_type pubKeyType = accountObject.owner.get_keys().get(0);
                if (pubKeyType != null) {
                    types.private_key_type privateKeyType = mHashMapPub2Priv.get(pubKeyType);
                    if (privateKeyType != null) {
                        result = privateKeyType.toString();
                    }
                }
            }
        }

        return result;
    }


    //根据账户ID 查账户名
    public String getAccountNameById(String account_id) {
        String result = "";
        for (int i = 0; i < list_my_accounts().size(); i++) {
            account_object accountObject = list_my_accounts().get(i);
            String accountid = accountObject.id.toString();
            if (accountid.equals(account_id)) {
                result = accountObject.name.toString();
                break;
            }
        }

        return result;
    }

    public int load_wallet_file(String strFileName, String password) {
        //web socket load wallet file
        try {
            FileInputStream fileInputStream = new FileInputStream(strFileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            Gson gson = global_config_object.getInstance().getGsonBuilder().create();
            mWalletObject = gson.fromJson(inputStreamReader, wallet_object.class);
            return 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return -1;
        } catch (JsonIOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int set_passwrod(String strPassword) {
        //for java wallet set_password
        mCheckSum = sha512_object.create_from_string(strPassword);

        lock();

        return 0;
    }

    public List<account_object> list_my_accounts() {
        List<account_object> accountObjectList = new ArrayList<>();
        if (mWalletObject != null) {
            accountObjectList.addAll(mWalletObject.my_accounts);
        }
        return accountObjectList;
    }

    public account_object get_account(String strAccountNameOrId) throws NetworkStatusException {
        // 判定这类型
        object_id<account_object> accountObjectId = object_id.create_from_string(strAccountNameOrId);

        List<account_object> listAccountObject;
        if (accountObjectId == null) {
            listAccountObject = lookup_account_names(strAccountNameOrId);

            if (listAccountObject == null) {
                return null;
            } else {
                if (listAccountObject.isEmpty()) {
                    return null;
                }

                return listAccountObject.get(0);
            }
        } else {
            List<object_id<account_object>> listAccountObjectId = new ArrayList<>();
            listAccountObjectId.add(accountObjectId);
            listAccountObject = mWebsocketApi.get_accounts(listAccountObjectId);
            if (listAccountObject == null) {
                return null;
            } else {
                if (listAccountObject.isEmpty()) {
                    return null;
                }

                return listAccountObject.get(0);
            }
        }
    }

    public List<account_object> get_accounts(List<object_id<account_object>> listAccountObjectId) throws NetworkStatusException {
        return mWebsocketApi.get_accounts(listAccountObjectId);
    }

    public List<account_object> lookup_account_names(String strAccountName) throws NetworkStatusException {
        return mWebsocketApi.lookup_account_names(strAccountName);
    }

    public List<asset> list_account_balance(object_id<account_object> accountId) throws NetworkStatusException {
        return mWebsocketApi.list_account_balances(accountId);
    }


    public List<operation_history_object> get_account_history(object_id<account_object> accountId, int nLimit) throws NetworkStatusException {
        return mWebsocketApi.get_account_history(accountId, nLimit);
    }

    public operation_types_histoy_object get_account_history_by_operations(String accountIdOrName, List<Integer> operation_types, int start, int nLimit) throws NetworkStatusException {
        object_id id = object_id.create_from_string(accountIdOrName);

        if (id == null) {
            List<account_object> list = lookup_account_names(accountIdOrName);

            if (list.size() == 0) return null;

            id = list.get(0).id;
        }

        return mWebsocketApi.get_account_history_by_operations(id, operation_types, start, nLimit);
    }

    public List<operation_history_object> get_account_history_operations_with_last_id(object_id<account_object> accountId, int operation, int nLimit,int id) throws NetworkStatusException {
        return mWebsocketApi.get_account_history_operations(accountId,operation,id,0,nLimit);
    }

    public List<operation_history_object> get_account_history_operations(object_id<account_object> accountId, int operation,int start,int stop, int nLimit) throws NetworkStatusException {
        return mWebsocketApi.get_account_history_operations(accountId,operation,start,stop,nLimit);
    }


    public List<operation_history_object> get_account_history_with_last_id(object_id<account_object> accountId, int nLimit,String id) throws NetworkStatusException {

        List<operation_history_object> list = mWebsocketApi.get_account_history_with_last_id(accountId, nLimit,id);

        for ( operation_history_object ob : list ) {
            if (ob.op.nOperationType == 0) {
                operations.transfer_operation transfer = (operations.transfer_operation) ob.op.operationContent;

                if (transfer.memo != null) {
                    ob.memo = decrypt_memo_message(transfer.memo);
                }

                asset_object asset = lookup_asset_symbols(transfer.amount.asset_id.toString());

                account_object from = mWebsocketApi.get_account(transfer.from.toString());
                account_object to = mWebsocketApi.get_account(transfer.to.toString());

                asset_object fee = lookup_asset_symbols(transfer.fee.asset_id.toString());

                ob.description = "Transfer " + CalculateUtils.div(transfer.amount.amount,Double.parseDouble(asset.get_scaled_precision()+""),5)  + " " + asset.symbol + " from " + from.name + " to " + to.name + " " + "(Fee: " + String.valueOf(transfer.fee.amount / Float.parseFloat(String.valueOf(fee.get_scaled_precision()))) + " " + fee.symbol + ")";
            }
        }

        return list;
    }

    public sha256_object get_chain_id() throws Exception {
        return mWebsocketApi.get_chain_id();
    }

    public List<asset_object> list_assets_obj(String strLowerBound, int nLimit) throws NetworkStatusException {
        return mWebsocketApi.list_assets(strLowerBound, nLimit);
    }

    public List<asset_object> get_assets(List<object_id<asset_object>> listAssetObjectId) throws NetworkStatusException {
        return mWebsocketApi.get_assets(listAssetObjectId);
    }

    public block_header get_block_header(int nBlockNumber) throws NetworkStatusException {
        return mWebsocketApi.get_block_header(nBlockNumber);
    }

    public block_object get_block(int nBlockNumber) throws NetworkStatusException {
        return mWebsocketApi.get_block(nBlockNumber);
    }

    public asset_object lookup_asset_symbols(String strAssetSymbol) throws NetworkStatusException {
        return mWebsocketApi.lookup_asset_symbols(strAssetSymbol);
    }

    public int import_brain_key(String strAccountNameOrId, String strBrainKey) throws NetworkStatusException,NoSuchAlgorithmException,UnsupportedEncodingException {
        account_object accountObject = get_account(strAccountNameOrId);
        if (accountObject == null) {
            return ErrorCode.ERROR_NO_ACCOUNT_OBJECT;
        }

        Map<types.public_key_type, types.private_key_type> mapPublic2Private = new HashMap<>();
        for (int i = 0; i < 10; ++i) {
            String encoded = String.format("%s %d", strBrainKey, i);
            /*
             根据速记词生成私钥
             */
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(encoded.getBytes("UTF-8"));
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] result = sha256.digest(bytes);
            ECKey ecKey = ECKey.fromPrivate(result);
            private_key privateKey = new private_key(ecKey.getPrivKeyBytes());
            types.private_key_type privateKeyType = new types.private_key_type(privateKey);
            types.public_key_type publicKeyType = new types.public_key_type(privateKey.get_public_key());

            if (!accountObject.active.is_public_key_type_exist(publicKeyType) &&
                    !accountObject.owner.is_public_key_type_exist(publicKeyType) &&
                    !accountObject.options.memo_key.compare(publicKeyType)) {
                continue;
            }
            mapPublic2Private.put(publicKeyType, privateKeyType);
        }

        if (mapPublic2Private.isEmpty()) return ErrorCode.ERROR_IMPORT_NOT_MATCH_PRIVATE_KEY;


        mWalletObject.update_account(accountObject);

        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.addAll(mapPublic2Private.keySet());

        mWalletObject.extra_keys.put(accountObject.id, listPublicKeyType);
        mHashMapPub2Priv.putAll(mapPublic2Private);

        encrypt_keys();

        return 0;
    }

    public int import_key(String account_name_or_id,
                          String wif_key) throws NetworkStatusException {
        if ((is_locked() && !is_new())) {
            return -1;
        }

        types.private_key_type privateKeyType = new types.private_key_type(wif_key);

        public_key publicKey = privateKeyType.getPrivateKey().get_public_key();
        types.public_key_type publicKeyType = new types.public_key_type(publicKey);

        account_object accountObject = get_account(account_name_or_id);
        if (accountObject == null) {
            return ErrorCode.ERROR_NO_ACCOUNT_OBJECT;
        }

        /*List<account_object> listAccountObject = lookup_account_names(account_name_or_id);
        // 进行publicKey的比对
        if (listAccountObject.isEmpty()) {
            return -1;
        }

        account_object accountObject = listAccountObject.get(0);*/
        if (!accountObject.active.is_public_key_type_exist(publicKeyType) &&
                !accountObject.owner.is_public_key_type_exist(publicKeyType) &&
                !accountObject.options.memo_key.compare(publicKeyType)) {
            return -1;
        }

        mWalletObject.update_account(accountObject);

        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.add(publicKeyType);

        mWalletObject.extra_keys.put(accountObject.id, listPublicKeyType);
        mHashMapPub2Priv.put(publicKeyType, privateKeyType);

        encrypt_keys();

        return 0;
    }

    public int import_keys(String account_name_or_id, String wif_key_1, String wif_key_2)
            throws NetworkStatusException {
        assert (is_locked() == false && is_new() == false);

        types.private_key_type privateKeyType1 = new types.private_key_type(wif_key_1);
        types.private_key_type privateKeyType2 = new types.private_key_type(wif_key_2);

        public_key publicKey1 = privateKeyType1.getPrivateKey().get_public_key();
        public_key publicKey2 = privateKeyType1.getPrivateKey().get_public_key();
        types.public_key_type publicKeyType1 = new types.public_key_type(publicKey1);
        types.public_key_type publicKeyType2 = new types.public_key_type(publicKey2);

        account_object accountObject = get_account(account_name_or_id);
        if (accountObject == null) {
            return ErrorCode.ERROR_NO_ACCOUNT_OBJECT;
        }

        /*List<account_object> listAccountObject = lookup_account_names(account_name_or_id);
        // 进行publicKey的比对
        if (listAccountObject.isEmpty()) {
            return -1;
        }

        account_object accountObject = listAccountObject.get(0);*/
        if (accountObject.active.is_public_key_type_exist(publicKeyType1) == false &&
                accountObject.owner.is_public_key_type_exist(publicKeyType1) == false &&
                accountObject.options.memo_key.compare(publicKeyType1) == false) {
            return -1;
        }

        if (accountObject.active.is_public_key_type_exist(publicKeyType2) == false &&
                accountObject.owner.is_public_key_type_exist(publicKeyType2) == false &&
                accountObject.options.memo_key.compare(publicKeyType2) == false) {
            return -1;
        }

        mWalletObject.update_account(accountObject);

        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.add(publicKeyType1);
        listPublicKeyType.add(publicKeyType2);

        mWalletObject.extra_keys.put(accountObject.id, listPublicKeyType);
        mHashMapPub2Priv.put(publicKeyType1, privateKeyType1);
        mHashMapPub2Priv.put(publicKeyType2, privateKeyType2);

        encrypt_keys();

        // 保存至文件
        return 0;
    }

    public int import_account_password(String strAccountName,
                                       String strPassword) throws NetworkStatusException {
        private_key privateActiveKey = private_key.from_seed(strAccountName + "active" + strPassword);
        private_key privateOwnerKey = private_key.from_seed(strAccountName + "owner" + strPassword);

        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());

        account_object accountObject = get_account(strAccountName);
        if (accountObject == null) {
            return ErrorCode.ERROR_NO_ACCOUNT_OBJECT;
        }

        if (accountObject.active.is_public_key_type_exist(publicActiveKeyType) == false &&
                accountObject.owner.is_public_key_type_exist(publicOwnerKeyType) == false) {
            return ErrorCode.ERROR_PASSWORD_INVALID;
        }

        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.add(publicActiveKeyType);
        listPublicKeyType.add(publicOwnerKeyType);
        mWalletObject.update_account(accountObject);
        mWalletObject.extra_keys.put(accountObject.id, listPublicKeyType);
        mHashMapPub2Priv.put(publicActiveKeyType, new types.private_key_type(privateActiveKey));
        mHashMapPub2Priv.put(publicOwnerKeyType, new types.private_key_type(privateOwnerKey));

        encrypt_keys();

        // 保存至文件
        return 0;
    }

    public asset transfer_calculate_fee(String strAmount,
                                        String strAssetSymbol,
                                        String strMemo) throws NetworkStatusException {
        object_id<asset_object> assetObjectId = object_id.create_from_string(strAssetSymbol);
        asset_object assetObject;
        if (assetObjectId == null) {
            assetObject = lookup_asset_symbols(strAssetSymbol);
        } else {
            List<object_id<asset_object>> listAssetObjectId = new ArrayList<>();
            listAssetObjectId.add(assetObjectId);
            assetObject = get_assets(listAssetObjectId).get(0);
        }

        if (assetObject == null) {
            return null;
        }

        operations.transfer_operation transferOperation = new operations.transfer_operation();
        transferOperation.from = new object_id<account_object>(0, account_object.class);//accountObjectFrom.id;
        transferOperation.to = new object_id<account_object>(0, account_object.class);
        transferOperation.amount = assetObject.amount_from_string(strAmount);
        transferOperation.extensions = new HashSet<>();
        /*if (TextUtils.isEmpty(strMemo) == false) {

        }*/

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = 0;
        operationType.operationContent = transferOperation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);
        tx.extensions = new HashSet<>();
        set_operation_fees(tx, get_global_properties().parameters.current_fees);

        return transferOperation.fee;
    }

    //add param amount_to_fee: 手续费
    //add param symbol_to_fee: 手续费货币符号
    public signed_transaction transfer(String strFrom,
                                       String strTo,
                                       String strAmount,
                                       String strAssetSymbol,
                                       String strMemo,
                                       String amount_to_fee,
                                       String symbol_to_fee) throws NetworkStatusException {

        object_id<asset_object> assetObjectId = object_id.create_from_string(strAssetSymbol);
        asset_object assetObject;
        if (assetObjectId == null) {
            assetObject = lookup_asset_symbols(strAssetSymbol);
        } else {
            List<object_id<asset_object>> listAssetObjectId = new ArrayList<>();
            listAssetObjectId.add(assetObjectId);
            assetObject = get_assets(listAssetObjectId).get(0);
        }

        if (assetObject == null) {
            return null;
        }
        account_object accountObjectFrom = get_account(strFrom);
        account_object accountObjectTo = get_account(strTo);
        if (accountObjectTo == null) {
            return null;
        }

        operations.transfer_operation transferOperation = new operations.transfer_operation();
        transferOperation.from = accountObjectFrom.id;
        transferOperation.to = accountObjectTo.id;
        transferOperation.amount = assetObject.amount_from_string(strAmount);

        transferOperation.extensions = new HashSet<>();
        if (!TextUtils.isEmpty(strMemo)) {
            transferOperation.memo = new memo_data();
            transferOperation.memo.from = accountObjectFrom.options.memo_key;
            transferOperation.memo.to = accountObjectTo.options.memo_key;

            types.private_key_type privateKeyType = mHashMapPub2Priv.get(accountObjectFrom.options.memo_key);
            if (privateKeyType == null || privateKeyType.getPrivateKey() == null) {
                return null;
            }
            transferOperation.memo.set_message(
                    privateKeyType.getPrivateKey(),
                    accountObjectTo.options.memo_key.getPublicKey(),
                    strMemo,
                    0
            );
            transferOperation.memo.get_message(
                    privateKeyType.getPrivateKey(),
                    accountObjectTo.options.memo_key.getPublicKey()
            );
        }

        if (!TextUtils.isEmpty(symbol_to_fee)) {
            object_id<asset_object> feeAssetObjectId = object_id.create_from_string(symbol_to_fee);
            List<object_id<asset_object>> listAssetObjectId = new ArrayList<>();
            listAssetObjectId.add(feeAssetObjectId);
            asset_object feeAssetObject = get_assets(listAssetObjectId).get(0);
            transferOperation.fee = feeAssetObject.amount_from_string(amount_to_fee);
        }

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_TRANSER_OPERATION;
        operationType.operationContent = transferOperation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);
        tx.extensions = new HashSet<>();

        if (transferOperation.fee == null) {

            set_operation_fees(tx, get_global_properties().parameters.current_fees);
        }


        //// TODO: 07/09/2017 tx.validate();
        return sign_transaction(tx);
    }

    public asset calculate_sell_asset_fee(String amountToSell, asset_object assetToSell,
                                          String minToReceive, asset_object assetToReceive,
                                          global_property_object globalPropertyObject) {
        operations.limit_order_create_operation op = new operations.limit_order_create_operation();

        op.amount_to_sell = assetToSell.amount_from_string(amountToSell);
        op.min_to_receive = assetToReceive.amount_from_string(minToReceive);

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_CREATE_LIMIT_ORDER_OPERATION;
        operationType.operationContent = op;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx, globalPropertyObject.parameters.current_fees);

        return op.fee;
    }

    public asset calculate_sell_fee(asset_object assetToSell, asset_object assetToReceive,
                                    double rate, double amount,
                                    global_property_object globalPropertyObject) {

        String strAmount = Double.toString(amount);
        String strMinToReceive = Double.toString(rate * amount);
        if (strAmount == null) {
            return null;
        }
        if (strMinToReceive == null) {
            return null;
        }

        return calculate_sell_asset_fee(strAmount, assetToSell,
                strMinToReceive, assetToReceive, globalPropertyObject);
    }

    public asset calculate_buy_fee(asset_object assetToReceive, asset_object assetToSell,
                                   double rate, double amount,
                                   global_property_object globalPropertyObject) {

        String strAmount = Double.toString(rate * amount);
        if (strAmount == null) {
            return null;
        }
        String strMinToReceive = Double.toString(amount);
        if (strMinToReceive == null) {
            return null;
        }
        return calculate_sell_asset_fee(strAmount, assetToSell,
                strMinToReceive, assetToReceive, globalPropertyObject);
    }

    public signed_transaction sell_asset(String amountToSell,
                                         String symbolToSell,
                                         String minToReceive,
                                         String symbolToReceive,
                                         int timeoutSecs,
                                         boolean fillOrKill,
                                         String amount_to_fee,
                                         String symbol_to_fee,
                                         int index) throws NetworkStatusException {
        // 这是用于出售的帐号
        operations.limit_order_create_operation op = new operations.limit_order_create_operation();
        account_object accountObject;
        if (index < list_my_accounts().size()) {
            accountObject = list_my_accounts().get(index);
            op.seller = accountObject.id;
        } else {

        }

        // 填充数据 && 防止科学计数法出现在字符串中
        BigDecimal db = new BigDecimal(minToReceive);
        String minToReceiveStr = db.toPlainString();
        BigDecimal db2 = new BigDecimal(amountToSell);
        String amountToSellStr = db2.toPlainString();

        asset_object assetToSell = lookup_asset_symbols(symbolToSell);
        asset_object assetToReceive = lookup_asset_symbols(symbolToReceive);
        if (assetToSell == null || assetToReceive == null) {
            return null;
        }

        op.amount_to_sell = assetToSell.amount_from_string(amountToSellStr);
        op.min_to_receive = assetToReceive.amount_from_string(minToReceiveStr);
        if (timeoutSecs > 0) {
            op.expiration = new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutSecs));
        } else {
            op.expiration = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(360));
        }
        op.fill_or_kill = fillOrKill;
        op.extensions = new HashSet<>();

        if (!TextUtils.isEmpty(symbol_to_fee)) {
            object_id<asset_object> feeAssetObjectId = object_id.create_from_string(symbol_to_fee);
            List<object_id<asset_object>> listAssetObjectId = new ArrayList<>();
            listAssetObjectId.add(feeAssetObjectId);
            asset_object feeAssetObject = get_assets(listAssetObjectId).get(0);
            op.fee = feeAssetObject.amount_from_string(amount_to_fee);
        }

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_CREATE_LIMIT_ORDER_OPERATION;
        operationType.operationContent = op;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        if (op.fee == null) {
            if (null != get_global_properties().parameters) {
                set_operation_fees(tx, get_global_properties().parameters.current_fees);
            } else {
                return null;
            }
        }

        return sign_transaction(tx);
    }

    public signed_transaction borrow_asset(String amount_to_borrow,String asset_symbol,String amount_to_collateral,int index) throws NetworkStatusException {
        operations.call_order_update_operation op = new operations.call_order_update_operation();

        account_object accountObject;
        if (index < list_my_accounts().size()) {
            accountObject = list_my_accounts().get(index);
            op.funding_account = accountObject.id;
        } else {

        }
        asset_object asset_borrow = lookup_asset_symbols(asset_symbol);

        object_id<asset_object> base_asset = new object_id<asset_object>(0,asset_object.class);

        ArrayList<object_id<asset_object>> list =  new ArrayList<object_id<asset_object>>();

        list.add(base_asset);

        asset_object asset_collateratl = get_assets(list).get(0);

        op.delta_debt = asset_borrow.amount_from_string(amount_to_borrow);

        op.delta_collateral = asset_collateratl.amount_from_string(amount_to_collateral);
        op.extensions = new HashSet<>();

//        ID_UPDATE_LMMIT_ORDER_OPERATION
        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_UPDATE_LMMIT_ORDER_OPERATION;
        operationType.operationContent = op;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);


        tx.extensions = new HashSet<>();
        if (op.fee == null) {
            if (null != get_global_properties().parameters) {
                set_operation_fees(tx, get_global_properties().parameters.current_fees);
            } else {
                return null;
            }
        }
        return sign_transaction(tx);
    }

    public signed_transaction create_transcation(operations.base_operation base_operation) throws NetworkStatusException {

//        ID_UPDATE_LMMIT_ORDER_OPERATION
        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = new  operations.operation_id_map().getIdByOperationObject(base_operation.getClass());
        operationType.operationContent = base_operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx, get_global_properties().parameters.current_fees);
        return sign_transaction(tx);
    }

    public signed_transaction sell(String symbolToSell, String symbolToReceive, double minToReceive,
                                   double amount, String amount_to_fee,
                                   String symbol_to_fee, int index) throws NetworkStatusException {

        return sell_asset(Double.toString(amount), symbolToSell, minToReceive + "",
                symbolToReceive, 0, false, amount_to_fee, symbol_to_fee, index);
    }

    public signed_transaction sell(String symbolToSell, String symbolToReceive, double rate,
                                   double amount, int timeoutSecs, String amount_to_fee,
                                   String symbol_to_fee, int index) throws NetworkStatusException {

        String minToReceive = Double.toString(rate * amount);
        if (minToReceive.contains(".")) {
            if (symbolToReceive.equalsIgnoreCase("BTC") || symbolToReceive.equalsIgnoreCase("LTC") || symbolToReceive.equalsIgnoreCase("ETH")) {

                minToReceive = NumberUtils.formatNumber8((Double.toString(rate * amount)) + "");
            } else {
//                minToReceive = NumberUtils.formatNumber((Double.toString(rate * amount)) + "");
            }
        }

        return sell_asset(Double.toString(amount), symbolToSell, minToReceive,
                symbolToReceive, timeoutSecs, false, amount_to_fee, symbol_to_fee, index);

        // return sell_asset(Double.toString(amount), symbolToSell, Double.toString(rate * amount),
        //         symbolToReceive, timeoutSecs, false,amount_to_fee,symbol_to_fee,index);
    }

    public signed_transaction buy(String symbolToReceive, String symbolToSell, double amountToSell,
                                  double amount, String amount_to_fee,
                                  String symbol_to_fee, int index) throws NetworkStatusException {

        return sell_asset(amountToSell + "", symbolToSell, Double.toString(amount),
                symbolToReceive, 0, false, amount_to_fee, symbol_to_fee, index);
    }

    public signed_transaction buy(String symbolToReceive, String symbolToSell, double rate,
                                  double amount, int timeoutSecs, String amount_to_fee,
                                  String symbol_to_fee, int index) throws NetworkStatusException {

        String amountToSell = Double.toString(rate * amount);
        if (amountToSell.contains(".")) {
            if (symbolToSell.equalsIgnoreCase("BTC") || symbolToSell.equalsIgnoreCase("LTC") || symbolToSell.equalsIgnoreCase("ETH")) {

                amountToSell = NumberUtils.formatNumber8((Double.toString(rate * amount)) + "");
            } else {
                amountToSell = NumberUtils.formatNumber((Double.toString(rate * amount)) + "");
            }
        }
        return sell_asset(amountToSell, symbolToSell, Double.toString(amount),
                symbolToReceive, 0, false, amount_to_fee, symbol_to_fee, index);
    }

    public signed_transaction cancel_order(object_id<limit_order_object> id)
            throws NetworkStatusException {
        operations.limit_order_cancel_operation op = new operations.limit_order_cancel_operation();
        op.fee_paying_account = mWebsocketApi.get_limit_order(id).seller;
        op.order = id;
        op.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_CANCEL_LMMIT_ORDER_OPERATION;
        operationType.operationContent = op;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx, get_global_properties().parameters.current_fees);

        return sign_transaction(tx);
    }

    public global_property_object get_global_properties() throws NetworkStatusException {
        return mWebsocketApi.get_global_properties();
    }

    public block_chain_info get_info() throws NetworkStatusException {
        block_chain_info bchain = new block_chain_info();

        global_property_object global_props = get_global_properties();
        if (global_props == null) {
            return null;
        }
        dynamic_global_property_object dynamic_props = get_dynamic_global_properties();
        if (dynamic_props == null) {
            return null;
        }

        Date currentDateTime;
        if (dynamic_props != null) {
            bchain.head_block_num = Integer.toString(dynamic_props.head_block_number);
            bchain.head_block_id = dynamic_props.head_block_id.toString();
            //计算时间差
            currentDateTime = new Date();
            long diff = currentDateTime.getTime() - dynamic_props.time.getTime();
            if (diff < 0) {
                diff = dynamic_props.time.getTime() - currentDateTime.getTime();
            }
            long sec = diff / 1000;
            bchain.head_block_age = sec + " second old";

            Date maintenanceDateTime = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                String dateStr = dynamic_props.next_maintenance_time.replace("T", " ");
                maintenanceDateTime = sdf.parse(dateStr);
                long diff_main = currentDateTime.getTime() - maintenanceDateTime.getTime();
                if (diff_main < 0) {
                    diff_main = maintenanceDateTime.getTime() - currentDateTime.getTime();
                }
                long hour = diff_main / (1000 * 60 * 60);
                bchain.next_maintenance_time = hour + " hours in the future";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (global_props != null) {
            bchain.active_committee_memberts_count = global_props.active_committee_members.size();
            bchain.active_witnesses_count = global_props.active_witnesses.size();
        }

        return bchain;
    }

    public dynamic_global_property_object get_dynamic_global_properties() throws NetworkStatusException {
        return mWebsocketApi.get_dynamic_global_properties();
    }

    public signed_transaction upgrade_account(String name, boolean upgrade_to_lifetime_member) throws NetworkStatusException {
        account_object object = mWebsocketApi.get_account(name);
        operations.account_upgrade_operation operation = new operations.account_upgrade_operation();
        operation.account_to_upgrade = object.id;
        operation.upgrade_to_lifetime_member = upgrade_to_lifetime_member;

        operation.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_UPGRADE_ACCOUNT_OPERATION;
        operationType.operationContent = operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx,get_global_properties().parameters.current_fees);
        //set_operation_fees(tx, get_global_properties().parameters.current_fees);
        return sign_transaction(tx);
    }

    public signed_transaction withdraw_vesting(String name_or_id, String vesting_name,String amount,String asset_symbol) throws NetworkStatusException {
//        account_object object = mWebsocketApi.get_account(name);
        //待修复
        object_id<vesting_balance_object> object_witness_id = object_id.create_from_string(name_or_id);
        vesting_balance_object vesting_object = null;
        if (object_witness_id == null) {
            return null;
        }

        account_object vesting_owner = get_account(vesting_name);

        if (vesting_owner == null) {
            return null;
        }

        asset_object symbol = lookup_asset_symbols(asset_symbol);

        if (symbol == null) {
            return null;
        }

        operations.withdraw_vesting_operation operation = new operations.withdraw_vesting_operation();
        operation.vesting_balance = object_witness_id;
        operation.owner = vesting_owner.id;

        operation.amount = symbol.amount_from_string(amount);


        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_VESTING_WITHDRAW_OPERATION;
        operationType.operationContent = operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx,get_global_properties().parameters.current_fees);
        //set_operation_fees(tx, get_global_properties().parameters.current_fees);
        return sign_transaction(tx);
    }

    public signed_transaction create_asset (String issuer,
                                            String symbol,
                                            short precision,
                                            asset_options common,
                                            bitasset_options bitasset_opts) throws NetworkStatusException {
        assert(lookup_asset_symbols(symbol) == null);
        List<account_object> account_list = lookup_account_names(issuer);
        assert(account_list != null);
        assert(account_list.size() != 0);
        account_object issuer_account = account_list.get(0);
        operations.asset_create_operation operation = new operations.asset_create_operation();

        operation.issuer = issuer_account.id;

        operation.precision = precision;

        operation.symbol = symbol;

        operation.common_options = common;

        operation.bitasset_opts = bitasset_opts;

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_ASSET_CREATE_OPERATION;
        operationType.operationContent = operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx,get_global_properties().parameters.current_fees);

        return sign_transaction(tx);
    }

    public signed_transaction change_account_key(String account,String pubkey) throws Exception {
        account_object account_object = get_account(account);

        assert (account_object != null);

        operations.account_update_operation operation = new operations.account_update_operation();

        operation.account = account_object.id;

        types.public_key_type public_key_type = new types.public_key_type(pubkey);

        operation.active = new authority(1, public_key_type, 1);
        operation.owner = new authority(1, public_key_type, 1);

        operation.new_options = account_object.options;

        operation.new_options.memo_key = public_key_type;

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_ACCOUNT_UPDATE_OPERATION;
        operationType.operationContent = operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx,get_global_properties().parameters.current_fees);

        return sign_transaction(tx);
    }

    public signed_transaction issue_asset(String to_account, String amount, String symbol,
                                          String memo) throws Exception {

        asset_object issue_asset = lookup_asset_symbols(symbol);

        assert(issue_asset == null);
        List<account_object> account_list = lookup_account_names(to_account);
        assert(account_list != null);
        assert(account_list.size() != 0);
        account_object to_account_obj = account_list.get(0);

        operations.asset_issue_operation operation = new operations.asset_issue_operation();

        operation.issuer = issue_asset.issuer;

        operation.asset_to_issue = issue_asset.amount_from_string(amount);

        operation.issue_to_account = to_account_obj.id;

        if (memo != null && memo.length() != 0) {
            account_list = lookup_account_names(issue_asset.issuer.toString());
            assert(account_list != null);
            assert(account_list.size() != 0);
            account_object from_account = account_list.get(0);

            operation.memo = new memo_data();

            types.private_key_type privateKeyType = mHashMapPub2Priv.get(from_account.options.memo_key);
//            if (privateKeyType == null || privateKeyType.getPrivateKey() == null) throw new Exception("没有对应账户的memo签名私钥");

            operation.memo.set_message(
                    privateKeyType.getPrivateKey(),
                    to_account_obj.options.memo_key.getPublicKey(),
                    memo,
                    0
            );
            operation.memo.get_message(
                    privateKeyType.getPrivateKey(),
                    to_account_obj.options.memo_key.getPublicKey()
            );
        }

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_ASSET_ISSUE_OPERATION;
        operationType.operationContent = operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx,get_global_properties().parameters.current_fees);

        return sign_transaction(tx);
    }

    public signed_transaction publish_asset_feed(String publishing_account,
                                                 String symbol,
                                                 long core_exchange_base_amount,
                                                 long core_exchange_quote_amount,
                                                 double maintenance_collateral_ratio,
                                                 double maximum_short_squeeze_ratio) throws Exception {
        assert(maintenance_collateral_ratio>=1&&maintenance_collateral_ratio<=10);
        assert(maximum_short_squeeze_ratio>=1&&maximum_short_squeeze_ratio<=10);

        List<account_object> account_list = lookup_account_names(publishing_account);

        assert(account_list != null);
        account_object account = account_list.get(0);
        assert(account!= null);
        asset_object asset = lookup_asset_symbols(symbol);
        asset_object asset_base = lookup_asset_symbols("1.3.0");
        assert(asset!=null);

        price_feed feed = new price_feed();

        feed.settlement_price = new price(new asset(core_exchange_base_amount,asset.id),new asset(core_exchange_quote_amount,asset_base.id));
        feed.core_exchange_rate = new price(new asset(core_exchange_base_amount,asset.id),new asset(core_exchange_quote_amount,asset_base.id));

        feed.maintenance_collateral_ratio = Double.valueOf(maintenance_collateral_ratio * 1000).shortValue();
        feed.maximum_short_squeeze_ratio = Double.valueOf(maximum_short_squeeze_ratio * 1000).shortValue();

        operations.asset_publish_feed_operation operation = new operations.asset_publish_feed_operation();

        operation.publisher = account.id;
        operation.asset_id = asset.id;

        operation.feed = feed;
        operation.extensions = new HashSet();

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_PUBLISHING_ASSET_FEED_OPERATION;
        operationType.operationContent = operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx,get_global_properties().parameters.current_fees);

        return sign_transaction(tx);

    }

    public signed_transaction create_account_with_pub_key(String publicKey,
                                                              String strAccountName,
                                                               String strRegistar,
                                                              String strReferrer,
                                                              int refferPercent) throws NetworkStatusException {

        if (refferPercent > 100 || refferPercent < 0) {
            return null;
        }

        types.public_key_type publicOwnerKey = null;
        types.public_key_type publicActiveKey = null;
        types.public_key_type publicMemoKey = null;
        try {
            publicMemoKey = new types.public_key_type(publicKey);
            publicActiveKey = new types.public_key_type(publicKey);
            publicOwnerKey = new types.public_key_type(publicKey);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        operations.account_create_operation operation = new operations.account_create_operation();
        operation.name = strAccountName;

        if (operation.options == null)
            operation.options = new types.account_options();

        operation.options.num_committee = 0;
        operation.options.num_witness = 0;
        operation.options.voting_account = new object_id<account_object>(1,2,5);
        operation.options.votes = new HashSet<>();
        operation.options.extensions = new HashSet<String>();
        operation.extensions = new HashMap();
        if (publicMemoKey != null)
            operation.options.memo_key = publicOwnerKey;

        operation.active = new authority(1, publicActiveKey, 1);
        operation.owner = new authority(1, publicOwnerKey, 1);

        account_object accountRegistrar = get_account(strRegistar);
        account_object accountReferr = get_account(strReferrer);

        operation.referrer = accountReferr.id;
        operation.registrar = accountRegistrar.id;
        operation.referrer_percent = refferPercent * 100;



        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_CREATE_ACCOUNT_OPERATION;
        operationType.operationContent = operation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx, get_global_properties().parameters.current_fees);

        return sign_create_account_transaction(tx);
    }

    private signed_transaction sign_create_account_transaction(signed_transaction tx) throws NetworkStatusException {
        // // TODO: 07/09/2017 这里的set应出问题
        signed_transaction.required_authorities requiresAuthorities = tx.get_required_authorities();

        Set<object_id<account_object>> req_active_approvals = new HashSet<>();
        req_active_approvals.addAll(requiresAuthorities.active);

        Set<object_id<account_object>> req_owner_approvals = new HashSet<>();
        req_owner_approvals.addAll(requiresAuthorities.owner);


        for (authority authorityObject : requiresAuthorities.other) {
            for (object_id<account_object> accountObjectId : authorityObject.account_auths().keySet()) {
                req_active_approvals.add(accountObjectId);
            }
        }

        Set<object_id<account_object>> accountObjectAll = new HashSet<>();
        accountObjectAll.addAll(req_active_approvals);
        accountObjectAll.addAll(req_owner_approvals);


        List<object_id<account_object>> listAccountObjectId = new ArrayList<>();
        listAccountObjectId.addAll(accountObjectAll);

        List<account_object> listAccountObject = get_accounts(listAccountObjectId);
        HashMap<object_id<account_object>, account_object> hashMapIdToObject = new HashMap<>();
        for (account_object accountObject : listAccountObject) {
            hashMapIdToObject.put(accountObject.id, accountObject);
        }

        HashSet<types.public_key_type> approving_key_set = new HashSet<>();
        for (object_id<account_object> accountObjectId : req_active_approvals) {
            account_object accountObject = hashMapIdToObject.get(accountObjectId);
            approving_key_set.addAll(accountObject.active.get_keys());
        }

        for (object_id<account_object> accountObjectId : req_owner_approvals) {
            account_object accountObject = hashMapIdToObject.get(accountObjectId);
            approving_key_set.addAll(accountObject.owner.get_keys());
        }

        for (authority authorityObject : requiresAuthorities.other) {
            for (types.public_key_type publicKeyType : authorityObject.get_keys()) {
                approving_key_set.add(publicKeyType);
            }
        }

        // // TODO: 07/09/2017 被简化了
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);

        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);

        dateObject = calender.getTime();

        tx.set_expiration(dateObject);

        for (types.public_key_type pulicKeyType : approving_key_set) {
            types.private_key_type privateKey = mHashMapPub2Priv.get(pulicKeyType);
            if (privateKey != null) {
                tx.sign(privateKey, mWalletObject.chain_id);
            }
        }

        // 发出tx，进行广播，这里也涉及到序列化
        int nRet = mWebsocketApi.broadcast_json_transaction(tx, "");
        if (nRet == 0) {
            return tx;
        } else {
            return null;
        }
    }

    private signed_transaction sign_transaction(signed_transaction tx) throws NetworkStatusException {
        // // TODO: 07/09/2017 这里的set应出问题
        signed_transaction.required_authorities requiresAuthorities = tx.get_required_authorities();

        Set<object_id<account_object>> req_active_approvals = new HashSet<>();
        req_active_approvals.addAll(requiresAuthorities.active);

        Set<object_id<account_object>> req_owner_approvals = new HashSet<>();
        req_owner_approvals.addAll(requiresAuthorities.owner);


        for (authority authorityObject : requiresAuthorities.other) {
            for (object_id<account_object> accountObjectId : authorityObject.account_auths().keySet()) {
                req_active_approvals.add(accountObjectId);
            }
        }

        Set<object_id<account_object>> accountObjectAll = new HashSet<>();
        accountObjectAll.addAll(req_active_approvals);
        accountObjectAll.addAll(req_owner_approvals);


        List<object_id<account_object>> listAccountObjectId = new ArrayList<>();
        listAccountObjectId.addAll(accountObjectAll);

        List<account_object> listAccountObject = get_accounts(listAccountObjectId);
        HashMap<object_id<account_object>, account_object> hashMapIdToObject = new HashMap<>();
        for (account_object accountObject : listAccountObject) {
            hashMapIdToObject.put(accountObject.id, accountObject);
        }

        HashSet<types.public_key_type> approving_key_set = new HashSet<>();
        for (object_id<account_object> accountObjectId : req_active_approvals) {
            account_object accountObject = hashMapIdToObject.get(accountObjectId);
            approving_key_set.addAll(accountObject.active.get_keys());
        }

        for (object_id<account_object> accountObjectId : req_owner_approvals) {
            account_object accountObject = hashMapIdToObject.get(accountObjectId);
            approving_key_set.addAll(accountObject.owner.get_keys());
        }

        for (authority authorityObject : requiresAuthorities.other) {
            for (types.public_key_type publicKeyType : authorityObject.get_keys()) {
                approving_key_set.add(publicKeyType);
            }
        }

        // // TODO: 07/09/2017 被简化了
        dynamic_global_property_object dynamicGlobalPropertyObject = get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);

        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 12 * 3600);

        dateObject = calender.getTime();

        tx.set_expiration(dateObject);

        for (types.public_key_type pulicKeyType : approving_key_set) {
            types.private_key_type privateKey = mHashMapPub2Priv.get(pulicKeyType);
            if (privateKey != null) {
                tx.sign(privateKey, mWalletObject.chain_id);
            }
        }

        // 发出tx，进行广播，这里也涉及到序列化
        int nRet = mWebsocketApi.broadcast_transaction(tx);
        if (nRet == 0) {
            return tx;
        } else {
            return null;
        }
    }

    public List<CallOrder> get_call_orders(object_id asset_id,Integer limit) throws NetworkStatusException {
        return mWebsocketApi.get_call_orders(asset_id,limit);
    }

    public List<bucket_object> get_market_history(object_id<asset_object> assetObjectId1,
                                                  object_id<asset_object> assetObjectId2,
                                                  int nBucket, Date dateStart, Date dateEnd)
            throws NetworkStatusException {
        return mWebsocketApi.get_market_history(assetObjectId1, assetObjectId2, nBucket, dateStart, dateEnd);
    }

    public MarketTicker get_ticker(String base, String quote) throws NetworkStatusException {
        return mWebsocketApi.get_ticker(base, quote);
    }

    private void set_operation_fees(signed_transaction tx, fee_schedule feeSchedule) {
        for (operations.operation_type operationType : tx.operations) {
            feeSchedule.set_fee(operationType, price.unit_price(new object_id<asset_object>(0, asset_object.class)));
        }
    }

    private private_key derive_private_key(String strWifKey, int nSeqNumber) {
        String strData = strWifKey + " " + nSeqNumber;
        byte[] bytesBuffer = strData.getBytes();
        SHA512Digest digest = new SHA512Digest();
        digest.update(bytesBuffer, 0, bytesBuffer.length);

        byte[] out = new byte[64];
        digest.doFinal(out, 0);

        SHA256Digest digest256 = new SHA256Digest();
        byte[] outSeed = new byte[32];
        byte[] outSeed2 = new byte[64];
        digest256.update(out, 0, out.length);
        digest.doFinal(outSeed2, 0);
        System.arraycopy(outSeed2, 0, outSeed, 0, 32);

        return new private_key(outSeed);
    }

    private int find_first_unused_derived_key_index(private_key privateKey) {
        int first_unused_index = 0;
        int number_of_consecutive_unused_keys = 0;

        String strWifKey = new types.private_key_type(privateKey).toString();
        for (int key_index = 0; ; ++key_index) {
            private_key derivedPrivateKey = derive_private_key(strWifKey, key_index);

            types.public_key_type publicKeyType = new types.public_key_type(derivedPrivateKey.get_public_key());

            if (mHashMapPub2Priv.containsKey(publicKeyType) == false) {
                if (number_of_consecutive_unused_keys != 0) {
                    ++number_of_consecutive_unused_keys;
                    if (number_of_consecutive_unused_keys > 5)
                        return first_unused_index;
                } else {
                    first_unused_index = key_index;
                    number_of_consecutive_unused_keys = 1;
                }
            } else {
                first_unused_index = 0;
                number_of_consecutive_unused_keys = 0;
            }
        }
    }

    public String decrypt_memo_message(memo_data memoData) {
        assert (is_locked() == false);
        String strMessage = null;

        types.private_key_type privateKeyType = mHashMapPub2Priv.get(memoData.to);
        if (privateKeyType != null) {
            strMessage = memoData.get_message(privateKeyType.getPrivateKey(), memoData.from.getPublicKey());
        } else {
            privateKeyType = mHashMapPub2Priv.get(memoData.from);
            if (privateKeyType != null) {
                strMessage = memoData.get_message(privateKeyType.getPrivateKey(), memoData.to.getPublicKey());
            }
        }

        return strMessage;
    }

    public List<limit_order_object> get_limit_orders(object_id<asset_object> base,
                                                     object_id<asset_object> quote,
                                                     int limit) throws NetworkStatusException {
        return mWebsocketApi.get_limit_orders(base, quote, limit);
    }

    public JsonElement get_bitasset_data(String symbol) throws NetworkStatusException {
        asset_object asset = lookup_asset_symbols(symbol);

        if (asset.bitasset_data_id == null) return null;

        return mWebsocketApi.get_object(asset.bitasset_data_id);
    }

    public JsonElement get_object(String object) throws NetworkStatusException {
        return mWebsocketApi.get_object(object_id.create_from_string(object));
    }

    public AllHistory get_all_history(String baseSymbolId, String qouteSymbolId, int nLimit)
        throws NetworkStatusException{
        return mWebsocketApi.get_all_history(baseSymbolId, qouteSymbolId,nLimit);
    }

    //获取撮合记录
    public OrderBook get_order_book(String base, String quote, int limit) throws NetworkStatusException {
        return mWebsocketApi.get_order_book(base,quote,limit);
    }

    public List<full_account_object> get_full_accounts(List<String> names, boolean subscribe)
            throws NetworkStatusException {
        return mWebsocketApi.get_full_accounts(names, subscribe);
    }

    public HashMap<types.public_key_type, types.private_key_type> get_wallet_hash() {
        return mHashMapPub2Priv;
    }
    //生成公私钥对
    public Map<private_key, public_key> generateKeyFromPassword(String account_name, String password) {
        Map<private_key, public_key> keys = new HashMap<private_key, public_key>();
        if (account_name != null && password != null) {
            String seed = account_name + "owner" + password;
            private_key privKey = private_key.from_seed(seed);
            public_key pubKey = privKey.get_public_key();
            keys.put(privKey, pubKey);
        }
        return keys;
    }
}