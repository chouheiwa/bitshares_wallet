package com.borderless.wallet.socket;

import com.borderless.wallet.constants.OwnerInfoConstant;
import com.borderless.wallet.net.model.AllHistory;
import com.borderless.wallet.net.model.HistoryResponseModel;
import com.borderless.wallet.socket.chain.*;
import com.borderless.wallet.socket.common.ErrorCode;
import com.borderless.wallet.socket.exception.NetworkStatusException;
import com.borderless.wallet.socket.market.MarketTicker;
import de.bitsharesmunich.graphenej.Address;
import de.bitsharesmunich.graphenej.Asset;
import de.bitsharesmunich.graphenej.FileBin;
//import de.bitsharesmunich.graphenej.errors.MalformedAddressException;
import de.bitsharesmunich.graphenej.models.backup.LinkedAccount;
import de.bitsharesmunich.graphenej.models.backup.WalletBackup;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BitsharesWalletWraper {
    private wallet_api mWalletApi;
    private Map<object_id<account_object>, account_object> mMapAccountId2Object = new ConcurrentHashMap<>();
    private Map<object_id<account_object>, List<asset>> mMapAccountId2Asset = new ConcurrentHashMap<>();
    private Map<object_id<account_object>, List<operation_history_object>> mMapAccountId2History = new ConcurrentHashMap<>();
    private Map<object_id<asset_object>, asset_object> mMapAssetId2Object = new ConcurrentHashMap<>();
    private String mstrWalletFilePath;
    private String chain_id;
    private String websocket_server;

    private int mnStatus = STATUS_INVALID;

    private static final int STATUS_INVALID = -1;
    private static final int STATUS_INITIALIZED = 0;

    private BitshareData mBitshareData;

    public BitsharesWalletWraper() {
        this.chain_id = OwnerInfoConstant.CHAIN_ID;
        this.setWebsocket_server(OwnerInfoConstant.BORDERLESS_BLOCK_SERVER);
    }

    public BitsharesWalletWraper(String chain_id,String prefix) {
        this.chain_id = chain_id;
        config.GRAPHENE_ADDRESS_PREFIX = prefix;
    }
    //设置新的websocket地址 (返回bool值成功失败)
    public Boolean setWebsocket_server(String websocket_server) {
        this.websocket_server = websocket_server;
        this.clear();
        return this.build_connect() == STATUS_INITIALIZED;
    }

    public void clear() {
        if (mWalletApi != null) {
            mWalletApi.reset();
        }
        mWalletApi = new wallet_api(websocket_server);
        mMapAccountId2Object.clear();
        mMapAccountId2Asset.clear();
        mMapAccountId2History.clear();
        mMapAssetId2Object.clear();
        mnStatus = STATUS_INVALID;
    }
    public void reset() {
        mWalletApi.reset();
        mWalletApi = new wallet_api(websocket_server);
        mMapAccountId2Object.clear();
        mMapAccountId2Asset.clear();
        mMapAccountId2History.clear();
        mMapAssetId2Object.clear();

        if (mstrWalletFilePath != null) {
            File file = new File(mstrWalletFilePath);
            file.delete();
        }

        mnStatus = STATUS_INVALID;
    }

    public account_object get_account() {
        List<account_object> listAccount = mWalletApi.list_my_accounts();
        if (listAccount == null || listAccount.isEmpty()) {
            return null;
        }

        return listAccount.get(0);
    }

    public boolean is_new() {
        return mWalletApi.is_new();
    }

    public  boolean is_locked() {
        return mWalletApi.is_locked();
    }

    public synchronized int build_connect() {
        if (mnStatus == STATUS_INITIALIZED) {
            return 0;
        }

        int nRet = mWalletApi.initialize(chain_id);
        if (nRet != 0) {
            return nRet;
        }

        mnStatus = STATUS_INITIALIZED;
        return 0;
    }

    public void close(){
        mWalletApi.close();
    }

    public void set_passwrod(String strPassword) {
        mWalletApi.set_passwrod(strPassword);
    }

    public int load_wallet_file(String strFileName, String password) {
        return mWalletApi.load_wallet_file(strFileName, password);
    }

    public List<account_object> list_my_accounts() {
        return mWalletApi.list_my_accounts();
    }

    public int import_key(String strAccountNameOrId,
                          String strPrivateKey) {
        try {
            int nRet = mWalletApi.import_key(strAccountNameOrId, strPrivateKey);
            if (nRet != 0) {
                return nRet;
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
            return -1;
        }

        for (account_object accountObject : list_my_accounts()) {
            mMapAccountId2Object.put(accountObject.id, accountObject);
        }

        return 0;
    }

    public int import_keys(String strAccountNameOrId, String strPrivateKey1, String strPrivateKey2) {

        try {
            int nRet = mWalletApi.import_keys(strAccountNameOrId, strPrivateKey1, strPrivateKey2);
            if (nRet != 0) {
                return nRet;
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
            return -1;
        }

//        save_wallet_file();

        for (account_object accountObject : list_my_accounts()) {
            mMapAccountId2Object.put(accountObject.id, accountObject);
        }

        return 0;
    }

    public int import_brain_key(String strAccountNameOrId, String strBrainKey) {
        try {
            int nRet = mWalletApi.import_brain_key(strAccountNameOrId, strBrainKey);
            if (nRet != 0) {
                return nRet;
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
            return ErrorCode.ERROR_IMPORT_NETWORK_FAIL;
        }

        //save_wallet_file();

        for (account_object accountObject : list_my_accounts()) {
            mMapAccountId2Object.put(accountObject.id, accountObject);
        }

        return 0;
    }

    public int import_file_bin(String strPassword, String strFilePath) {
        File file = new File(strFilePath);
        if (file.exists() == false) {
            return ErrorCode.ERROR_FILE_NOT_FOUND;
        }

        int nSize = (int)file.length();

        final byte[] byteContent = new byte[nSize];

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteContent, 0, byteContent.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ErrorCode.ERROR_FILE_NOT_FOUND;
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorCode.ERROR_FILE_READ_FAIL;
        }

        WalletBackup walletBackup = FileBin.deserializeWalletBackup(byteContent, strPassword);
        if (walletBackup == null) {
            return ErrorCode.ERROR_FILE_BIN_PASSWORD_INVALID;
        }

        String strBrainKey = walletBackup.getWallet(0).decryptBrainKey(strPassword);
        //LinkedAccount linkedAccount = walletBackup.getLinkedAccounts()[0];

        int nRet = ErrorCode.ERROR_IMPORT_NOT_MATCH_PRIVATE_KEY;
        for (LinkedAccount linkedAccount : walletBackup.getLinkedAccounts()) {
            nRet = import_brain_key(linkedAccount.getName(), strBrainKey);
            if (nRet == 0) {
                break;
            }
        }

        return nRet;
    }

    public int unlock(String strPassword) {
        return mWalletApi.unlock(strPassword);
    }

    public signed_transaction create_account_with_pub_key(String pubKey, String strAccountName,
                                                 String strRegistar, String strReferrer, int refferPercent)
            throws NetworkStatusException {
        return mWalletApi.create_account_with_pub_key(pubKey,strAccountName,strRegistar,strReferrer,refferPercent);
    }

    public signed_transaction upgrade_account(String name, boolean upgrade_to_lifetime_member)throws NetworkStatusException  {
        return  mWalletApi.upgrade_account(name,upgrade_to_lifetime_member);
    }

    public signed_transaction with_draw_vesting(String name_or_id, String vesting_name,String amount,String asset_symbol) throws NetworkStatusException {
        return mWalletApi.withdraw_vesting(name_or_id,vesting_name,amount,asset_symbol);
    }

    public signed_transaction create_asset (String issuer,
                                            String symbol,
                                            short precision,
                                            asset_options common,
                                            bitasset_options bitasset_opts) throws NetworkStatusException {
        return mWalletApi.create_asset(issuer, symbol, precision, common, bitasset_opts);
    }

    public signed_transaction publish_asset_feed(String publishing_account,
                                                 String symbol,
                                                 long core_exchange_base_amount,
                                                 long core_exchange_quote_amount,
                                                 double maintenance_collateral_ratio,
                                                 double maximum_short_squeeze_ratio) throws NetworkStatusException {
        return mWalletApi.publish_asset_feed(publishing_account, symbol, core_exchange_base_amount, core_exchange_quote_amount, maintenance_collateral_ratio, maximum_short_squeeze_ratio);
    }

    public int lock() {
        return mWalletApi.lock();
    }

    public List<asset> list_balances(boolean bRefresh) throws NetworkStatusException {
        List<asset> listAllAsset = new ArrayList<>();
        for (account_object accountObject : list_my_accounts()) {
            List<asset> listAsset = list_account_balance(accountObject.id, bRefresh);

            listAllAsset.addAll(listAsset);
        }

        return listAllAsset;
    }

    public List<asset> list_account_balance(object_id<account_object> accountObjectId,
                                            boolean bRefresh) throws NetworkStatusException {
        List<asset> listAsset = null;
        if (mMapAccountId2Asset != null) {
            listAsset = mMapAccountId2Asset.get(accountObjectId);
            if (bRefresh || listAsset == null) {
                listAsset = mWalletApi.list_account_balance(accountObjectId);
                mMapAccountId2Asset.put(accountObjectId, listAsset);
                return listAsset;
            }
        } else {
            return null;
        }
        return listAsset;

    }

    public List<Asset> list_assets(String strLowerBound, int nLimit) throws NetworkStatusException {
        return mWalletApi.list_assets(strLowerBound, nLimit);
    }

    public List<asset_object> list_assets_obj(String strLowerBound, int nLimit) throws NetworkStatusException {
        return mWalletApi.list_assets_obj(strLowerBound, nLimit);
    }

    public Map<object_id<asset_object>, asset_object> get_assets(List<object_id<asset_object>> listAssetObjectId) throws NetworkStatusException {
        Map<object_id<asset_object>, asset_object> mapId2Object = new HashMap<>();

        List<object_id<asset_object>> listRequestId = new ArrayList<>();
        for (object_id<asset_object> objectId : listAssetObjectId) {
            asset_object assetObject = mMapAssetId2Object.get(objectId);
            if (assetObject != null) {
                mapId2Object.put(objectId, assetObject);
            } else {
                listRequestId.add(objectId);
            }
        }

        if (listRequestId.isEmpty() == false) {
            List<asset_object> listAssetObject = mWalletApi.get_assets(listRequestId);
            for (asset_object assetObject : listAssetObject) {
                mapId2Object.put(assetObject.id, assetObject);
                mMapAssetId2Object.put(assetObject.id, assetObject);
            }
        }

        return mapId2Object;
    }

    public asset_object lookup_asset_symbols(String strAssetSymbol) throws NetworkStatusException {
        return mWalletApi.lookup_asset_symbols(strAssetSymbol);
    }

    public String lookup_asset_symbols_rate(String strAssetSymbol) throws NetworkStatusException {
        return mWalletApi.lookup_asset_symbols_rate(strAssetSymbol);
    }

    public String  get_Fee(String id,int op)  throws NetworkStatusException  {
        return mWalletApi.get_Fee(id,op);
    }

    public Map<object_id<account_object>, account_object> get_accounts(List<object_id<account_object>> listAccountObjectId) throws NetworkStatusException {
        Map<object_id<account_object>, account_object> mapId2Object = new HashMap<>();

        List<object_id<account_object>> listRequestId = new ArrayList<>();
        for (object_id<account_object> objectId : listAccountObjectId) {
            account_object accountObject = mMapAccountId2Object.get(objectId);
            if (accountObject != null) {
                mapId2Object.put(objectId, accountObject);
            } else {
                listRequestId.add(objectId);
            }
        }

        if (listRequestId.isEmpty() == false) {
            List<account_object> listAccountObject = mWalletApi.get_accounts(listRequestId);
            for (account_object accountObject : listAccountObject) {
                mapId2Object.put(accountObject.id, accountObject);
                mMapAccountId2Object.put(accountObject.id, accountObject);
            }
        }

        return mapId2Object;
    }

    public signed_transaction transfer(String strFrom, String strTo, String strAmount, String strAssetSymbol,
                                       String strMemo, String amount_to_fee, String symbol_to_fee)
            throws NetworkStatusException {
        signed_transaction signedTransaction = mWalletApi.transfer(strFrom,  strTo, strAmount, strAssetSymbol, strMemo, amount_to_fee, symbol_to_fee);
        return signedTransaction;
    }

    public signed_transaction borrow_asset(String amount_to_borrow,String asset_symbol,String amount_to_collateral,int index) throws NetworkStatusException {
        return mWalletApi.borrow_asset(amount_to_borrow,asset_symbol,amount_to_collateral,index);
    }

    // 获取区块信息
    public block_chain_info info() throws NetworkStatusException {
        return mWalletApi.get_info();
    }

    //获取块信息
    public block_object get_block(int  nblocknum) throws  NetworkStatusException {

        return mWalletApi.get_block(nblocknum);
    }

    // 获取对于基础货币的所有市场价格
    public Map<object_id<asset_object>, List<bucket_object>> get_market_histories_base(List<object_id<asset_object>> listAssetObjectId
    ,Integer bugket,Date dateObjectStart, Date dateObjectEnd
    ) throws NetworkStatusException {
//        dynamic_global_property_object dynamicGlobalPropertyObject = mWalletApi.get_dynamic_global_properties();
//
//        Date dateObject = dynamicGlobalPropertyObject.time;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dateObject);
//        calendar.add(Calendar.HOUR, -12);
//
//        Date dateObjectStart = calendar.getTime();
//
//        calendar.setTime(dateObject);
//        calendar.add(Calendar.SECOND, 30);
//
//        Date dateObjectEnd = calendar.getTime();
//
        Map<object_id<asset_object>, List<bucket_object>> mapId2BucketObject = new HashMap<>();

        object_id<asset_object> assetObjectBase = new object_id<asset_object>(0, asset_object.class);
        for (object_id<asset_object> objectId : listAssetObjectId) {
            if (objectId.equals(assetObjectBase)) {
                continue;
            }
            List<bucket_object> listBucketObject = mWalletApi.get_market_history(
                    objectId,
                    assetObjectBase,
                    bugket,
                    dateObjectStart,
                    dateObjectEnd
            );

            if (listBucketObject.isEmpty() == false) {
                //bucket_object bucketObject = listBucketObject.get(listBucketObject.size() - 1);
                mapId2BucketObject.put(objectId, listBucketObject);
            }
        }
        
        return mapId2BucketObject;
    }

    public block_header get_block_header(int nBlockNumber) throws NetworkStatusException {
        return mWalletApi.get_block_header(nBlockNumber);
    }

    public List<operation_history_object> get_history(boolean bRefresh) throws NetworkStatusException {
        List<operation_history_object> listAllHistoryObject = new ArrayList<>();
        for (account_object accountObject : list_my_accounts()) {
            List<operation_history_object> listHistoryObject = get_account_history(
                    accountObject.id,
                    100,
                    bRefresh
            );

            listAllHistoryObject.addAll(listHistoryObject);
        }

        return listAllHistoryObject;
    }

    public List<operation_history_object> get_account_history(object_id<account_object> accountObjectId,
                                                              int nLimit,
                                                              boolean bRefresh) throws NetworkStatusException {
        List<operation_history_object> listHistoryObject = mMapAccountId2History.get(accountObjectId);
        if (listHistoryObject == null || bRefresh) {
            listHistoryObject = mWalletApi.get_account_history(accountObjectId, nLimit);
            if (listHistoryObject == null) {
                return null;
            }

            mMapAccountId2History.put(accountObjectId, listHistoryObject);
        }
        return listHistoryObject;
    }

    public BitshareData prepare_data_to_display(boolean bRefresh,String quotoSymbo,Integer bugket,Date dateStart, Date dateEnd,BitsharesWalletWraper bww) {
        try {
            List<asset> listBalances = bww.list_balances(bRefresh);

            //List<operation_history_object> operationHistoryObjectList = BitsharesWalletWraper.getInstance().get_history(bRefresh);
            HashSet<object_id<account_object>> hashSetObjectId = new HashSet<object_id<account_object>>();
            HashSet<object_id<asset_object>> hashSetAssetObject = new HashSet<object_id<asset_object>>();

            //List<Pair<operation_history_object, Date>> listHistoryObjectTime = new ArrayList<Pair<operation_history_object, Date>>();
//            for (operation_history_object historyObject : operationHistoryObjectList) {
//                block_header blockHeader = BitsharesWalletWraper.getInstance().get_block_header(historyObject.block_num);
//                listHistoryObjectTime.add(new Pair<>(historyObject, blockHeader.timestamp));
//                if (historyObject.op.nOperationType <= operations.ID_CREATE_ACCOUNT_OPERATION) {
//                    operations.base_operation operation = (operations.base_operation)historyObject.op.operationContent;
//                    hashSetObjectId.addAll(operation.get_account_id_list());
//                    hashSetAssetObject.addAll(operation.get_asset_id_list());
//                }
//            }

            // 保证默认数据一直存在
            hashSetAssetObject.add(new object_id<asset_object>(0, asset_object.class));

            //// TODO: 06/09/2017 这里需要优化到一次调用

            for (asset assetBalances : listBalances) {
                hashSetAssetObject.add(assetBalances.asset_id);
            }

            List<object_id<account_object>> listAccountObjectId = new ArrayList<object_id<account_object>>();
            listAccountObjectId.addAll(hashSetObjectId);
            Map<object_id<account_object>, account_object> mapId2AccountObject =
                    bww.get_accounts(listAccountObjectId);


            List<object_id<asset_object>> listAssetObjectId = new ArrayList<object_id<asset_object>>();
            listAssetObjectId.addAll(hashSetAssetObject);

            // 生成id 2 asset_object映身
//            Map<object_id<asset_object>, asset_object> mapId2AssetObject =
//                    BitsharesWalletWraper.getInstance().get_assets(listAssetObjectId);

            asset_object currencyObject = mWalletApi.list_assets_obj(quotoSymbo, 1).get(0);
            //mapId2AssetObject.put(currencyObject.id, currencyObject);

            hashSetAssetObject.add(currencyObject.id);

            listAssetObjectId.clear();
            listAssetObjectId.addAll(hashSetAssetObject);

            Map<object_id<asset_object>, List<bucket_object>> mapAssetId2Bucket = get_market_histories_base(listAssetObjectId,bugket,dateStart,dateEnd);

            mBitshareData = new BitshareData();
            mBitshareData.assetObjectCurrency = currencyObject;
            mBitshareData.listBalances = listBalances;
            //mBitshareData.listHistoryObject = listHistoryObjectTime;
            //mBitshareData.mapId2AssetObject = mapId2AssetObject;
            mBitshareData.mapId2AccountObject = mapId2AccountObject;
            mBitshareData.mapAssetId2Bucket = mapAssetId2Bucket;

            return mBitshareData;

        } catch (NetworkStatusException e) {
            e.printStackTrace();
        }

        return null;
    }

    public signed_transaction sell_asset(String amountToSell, String symbolToSell,
                                         String minToReceive, String symbolToReceive,
                                         int timeoutSecs, boolean fillOrKill,String amount_to_fee,String symbol_to_fee,int index)
            throws NetworkStatusException {
        return mWalletApi.sell_asset(amountToSell, symbolToSell, minToReceive, symbolToReceive,
                timeoutSecs, fillOrKill,amount_to_fee,symbol_to_fee,index);
    }

    public asset calculate_sell_fee(asset_object assetToSell, asset_object assetToReceive,
                                    double rate, double amount,
                                    global_property_object globalPropertyObject) {

        return mWalletApi.calculate_sell_fee(assetToSell, assetToReceive, rate, amount,
                globalPropertyObject);
    }

    public asset calculate_buy_fee(asset_object assetToReceive, asset_object assetToSell,
                                   double rate, double amount,
                                   global_property_object globalPropertyObject) {
        return mWalletApi.calculate_buy_fee(assetToReceive, assetToSell, rate, amount,
                globalPropertyObject);
    }

    public signed_transaction sell(String quote, String base, double minToReceive, double amount, String amount_to_fee,
                                   String symbol_to_fee,int index)
            throws NetworkStatusException {
        return mWalletApi.sell(quote, base, minToReceive, amount,amount_to_fee,symbol_to_fee,index);
    }

    public signed_transaction sell(String base, String quote, double rate, double amount,
                                   int timeoutSecs, String amount_to_fee,
                                   String symbol_to_fee,int index) throws NetworkStatusException {
        return mWalletApi.sell(base, quote, rate, amount, timeoutSecs,amount_to_fee,symbol_to_fee,index);
    }

    public signed_transaction buy(  String quote, String base , double amountToSell, double amount, String amount_to_fee,
                                  String symbol_to_fee,int index)
            throws NetworkStatusException {
        return mWalletApi.buy(quote, base, amountToSell, amount,amount_to_fee,symbol_to_fee,index);
    }

    public signed_transaction buy(String base, String quote, double rate, double amount,
                                  int timeoutSecs, String amount_to_fee,
                                  String symbol_to_fee,int index) throws NetworkStatusException {
        return mWalletApi.buy(base, quote, rate, amount, timeoutSecs,amount_to_fee,symbol_to_fee,index);
    }

    public BitshareData getBitshareData() {
        return mBitshareData;
    }

    public account_object get_account_object(String strAccount) throws NetworkStatusException {
        return mWalletApi.get_account(strAccount);
    }

    public asset transfer_calculate_fee(String strAmount,
                                        String strAssetSymbol,
                                        String strMemo) throws NetworkStatusException {
        return mWalletApi.transfer_calculate_fee(strAmount, strAssetSymbol, strMemo);
    }

    public String get_plain_text_message(memo_data memoData) {
        return mWalletApi.decrypt_memo_message(memoData);
    }

    public full_account get_full_account(String name, boolean subscribe) throws NetworkStatusException, JSONException {
        return mWalletApi.get_full_account(name, subscribe);
    }

    public List<full_account_object> get_full_accounts(List<String> names, boolean subscribe)
            throws NetworkStatusException {
        return mWalletApi.get_full_accounts(names, subscribe);
    }

    public signed_transaction cancel_order(object_id<limit_order_object> id)
            throws NetworkStatusException {
        return mWalletApi.cancel_order(id);
    }

    public global_property_object get_global_properties() throws NetworkStatusException {
        return mWalletApi.get_global_properties();
    }

    public HashMap<types.public_key_type, types.private_key_type> get_wallet_hash() {
        return  mWalletApi.get_wallet_hash();
    }

    public boolean is_public_key_registered(String pub_key) throws NetworkStatusException {
        return mWalletApi.is_public_key_registered(pub_key);
    }

    //查询矿池总量
    public long get_witness_budget() throws NetworkStatusException {
        long witness_budget = 0;
        dynamic_global_property_object  dynamic_obj =  mWalletApi.get_dynamic_global_properties();
        if (dynamic_obj == null) {
            return witness_budget;
        } else {
            witness_budget = dynamic_obj.witness_budget;
        }
        return witness_budget;
    }

    /**
     * @return
     */
    public boolean is_suggest_brain_key(String accountName) {
        String suggestPublicKey = "";
        String suggestPrivateKey = "";
            try {
                BitsharesWalletWraper bww = new BitsharesWalletWraper();
                account_object account = bww.get_account_object(accountName);
                if (account != null) {
                    types.public_key_type pubKeyType = account.owner.get_keys().get(0);
                    if (pubKeyType != null) {
                        suggestPublicKey = pubKeyType.toString();
                        types.private_key_type privateKeyType = bww.get_wallet_hash().get(pubKeyType);
                        if (privateKeyType != null) {
                            suggestPrivateKey = privateKeyType.toString();
                        } else{
                            privateKeyType = bww.get_wallet_hash().get(pubKeyType);
                            if (privateKeyType != null) {
                                suggestPrivateKey = privateKeyType.toString();
                            }
                        }

                    }
                }
            } catch (NetworkStatusException e) {
                e.printStackTrace();
            }

            if (suggestPublicKey.equals("") || suggestPrivateKey.equals("")){
                return false;
            }else {
                return true;
            }
    }


    public String getSignMessage(String public_key, String accountName, String sign){
        String signMessage = "";
        account_object myAccount = null;
        //获取账户 private key
        BitsharesWalletWraper bww = new BitsharesWalletWraper();
        try {
            myAccount = bww.get_account_object(accountName);
        } catch (NetworkStatusException e) {
            e.printStackTrace();
        }
        //网络异常
        if (null == myAccount) {
            return signMessage;
        }
        types.public_key_type publicKeyType =  myAccount.owner.get_keys().get(0);
        types.private_key_type privateKey = bww.get_wallet_hash().get(publicKeyType);

        if(privateKey == null || privateKey.getPrivateKey() == null){
            return signMessage;
        }

        //请求public key
        memo_data memo = new memo_data();
        memo.from = myAccount.options.memo_key;
        memo.to = myAccount.options.memo_key;


        Address address = null;
        try {
            address = new Address(public_key);
        } catch (Exception e) {
            e.printStackTrace();
            return signMessage;
        }
        public_key publicKey = new public_key(address.getPublicKey().toBytes());
        //加密
        memo.set_message(privateKey.getPrivateKey(), publicKey, sign, 1);
        signMessage = memo.get_message_data();

        return signMessage;
    }
    public List<account_object> lookup_account_names(String strAccountName) throws NetworkStatusException {
        return mWalletApi.lookup_account_names(strAccountName);
    }

    public List<operation_history_object> get_account_history_with_last_id(String accountId, int nLimit,String id) throws NetworkStatusException {
        object_id id_object = object_id.create_from_string(accountId);

        if (id_object == null) {
            id_object = get_account_object(accountId).id;
        }

        return mWalletApi.get_account_history_with_last_id(id_object,nLimit,id);
    }


    public List<HistoryResponseModel.DataBean> get_transfer_history(object_id<account_object> accountObjectId,
                                                                    int nLimit) throws NetworkStatusException ,JSONException {
        List<HistoryResponseModel.DataBean> listHistoryObject;

        listHistoryObject = mWalletApi.get_transfer_history(accountObjectId, nLimit);

        return listHistoryObject;
    }

    /** add byzhaozhenhai
     * 根据链记录id为标记查询用户历史转账记录
     * @param accountObjectId
     * @param nLimit
     * @param flag
     * @return
     * @throws NetworkStatusException
     * @throws JSONException
     */
    public List<HistoryResponseModel.DataBean> get_transfer_history_by_flag(object_id<account_object> accountObjectId,
                                                                            int nLimit,String flag) throws NetworkStatusException ,JSONException {
        List<HistoryResponseModel.DataBean> listHistoryObject;

        listHistoryObject = mWalletApi.get_transfer_history_by_flag(accountObjectId, nLimit, flag);

        return listHistoryObject;
    }
    /**
     * 24小时成交记录
     * @param base
     * @param quote
     * @return
     * @throws NetworkStatusException
     */
    public MarketTicker get_ticker(String base, String quote) throws NetworkStatusException {
        return mWalletApi.get_ticker(base, quote);
    }

    public String get_bitasset_data(String symbol) throws NetworkStatusException {
        return mWalletApi.get_bitasset_data(symbol);
    }

    public AllHistory get_all_history(String baseSymbolId, String qouteSymbolId, int nLimit) throws NetworkStatusException {
        return mWalletApi.get_all_history(baseSymbolId, qouteSymbolId,nLimit);
    }

    public com.borderless.wallet.socket.market.OrderBook get_order_book(String base, String quote, int limit) throws  NetworkStatusException{
        return  mWalletApi.get_order_book(base,quote,limit);
    }
}
