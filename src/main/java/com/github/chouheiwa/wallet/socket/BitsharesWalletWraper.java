package com.github.chouheiwa.wallet.socket;

import com.github.chouheiwa.wallet.constants.OwnerInfoConstant;
import com.github.chouheiwa.wallet.net.model.AllHistory;
import com.github.chouheiwa.wallet.net.model.HistoryResponseModel;
import com.github.chouheiwa.wallet.socket.chain.*;
import com.github.chouheiwa.wallet.socket.common.ErrorCode;
import com.github.chouheiwa.wallet.socket.exception.NetworkStatusException;
import com.github.chouheiwa.wallet.socket.fc.crypto.sha256_object;
import com.github.chouheiwa.wallet.socket.market.MarketTicker;
import com.github.chouheiwa.wallet.socket.market.OrderBook;
import com.google.gson.JsonElement;

import java.io.File;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

//        save_wallet_file();

        for (account_object accountObject : list_my_accounts()) {
            mMapAccountId2Object.put(accountObject.id, accountObject);
        }

        return 0;
    }

    public List<limit_order_object> get_limit_orders(object_id<asset_object> base,
                                                     object_id<asset_object> quote,
                                                     int limit) throws Exception {
        return mWalletApi.get_limit_orders(base, quote, limit);
    }

    public int import_brain_key(String strAccountNameOrId, String strBrainKey) {
        try {
            int nRet = mWalletApi.import_brain_key(strAccountNameOrId, strBrainKey);
            if (nRet != 0) {
                return nRet;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCode.ERROR_IMPORT_NETWORK_FAIL;
        }

        //save_wallet_file();

        for (account_object accountObject : list_my_accounts()) {
            mMapAccountId2Object.put(accountObject.id, accountObject);
        }

        return 0;
    }

    public int unlock(String strPassword) {
        return mWalletApi.unlock(strPassword);
    }

    public sha256_object get_chain_id() throws Exception {
        return mWalletApi.get_chain_id();
    }

    public signed_transaction create_account_with_pub_key(String pubKey, String strAccountName,
                                                          String strRegistar, String strReferrer, int refferPercent)
            throws Exception {
        return mWalletApi.create_account_with_pub_key(pubKey,strAccountName,strRegistar,strReferrer,refferPercent);
    }

    public signed_transaction upgrade_account(String name, boolean upgrade_to_lifetime_member)throws Exception  {
        return  mWalletApi.upgrade_account(name,upgrade_to_lifetime_member);
    }

    public signed_transaction with_draw_vesting(String name_or_id, String vesting_name,String amount,String asset_symbol) throws Exception {
        return mWalletApi.withdraw_vesting(name_or_id,vesting_name,amount,asset_symbol);
    }

    public signed_transaction create_asset (String issuer,
                                            String symbol,
                                            short precision,
                                            asset_options common,
                                            bitasset_options bitasset_opts) throws Exception {
        return mWalletApi.create_asset(issuer, symbol, precision, common, bitasset_opts);
    }

    public signed_transaction issue_asset(String to_account, String amount, String symbol,
                                          String memo) throws Exception {
        return mWalletApi.issue_asset(to_account, amount, symbol, memo);
    }

    public signed_transaction publish_asset_feed(String publishing_account,
                                                 String symbol,
                                                 long core_exchange_base_amount,
                                                 long core_exchange_quote_amount,
                                                 double maintenance_collateral_ratio,
                                                 double maximum_short_squeeze_ratio) throws Exception {
        return mWalletApi.publish_asset_feed(publishing_account, symbol, core_exchange_base_amount, core_exchange_quote_amount, maintenance_collateral_ratio, maximum_short_squeeze_ratio);
    }

    public int lock() {
        return mWalletApi.lock();
    }

    public List<asset> list_balances(boolean bRefresh) throws Exception {
        List<asset> listAllAsset = new ArrayList<>();
        for (account_object accountObject : list_my_accounts()) {
            List<asset> listAsset = list_account_balance(accountObject.id, bRefresh);

            listAllAsset.addAll(listAsset);
        }

        return listAllAsset;
    }

    public List<asset> list_account_balance(object_id<account_object> accountObjectId,
                                            boolean bRefresh) throws Exception {
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
    public List<asset_object> list_assets_obj(String strLowerBound, int nLimit) throws Exception {
        return mWalletApi.list_assets_obj(strLowerBound, nLimit);
    }

    public Map<object_id<asset_object>, asset_object> get_assets(List<object_id<asset_object>> listAssetObjectId) throws Exception {
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

        if (!listRequestId.isEmpty()) {
            List<asset_object> listAssetObject = mWalletApi.get_assets(listRequestId);
            for (asset_object assetObject : listAssetObject) {
                mapId2Object.put(assetObject.id, assetObject);
                mMapAssetId2Object.put(assetObject.id, assetObject);
            }
        }

        return mapId2Object;
    }

    public asset_object lookup_asset_symbols(String strAssetSymbol) throws Exception {
        return mWalletApi.lookup_asset_symbols(strAssetSymbol);
    }

    public JsonElement get_object(String object) throws Exception {
        return mWalletApi.get_object(object);
    }

    public Map<object_id<account_object>, account_object> get_accounts(List<object_id<account_object>> listAccountObjectId) throws Exception {
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

        if (!listRequestId.isEmpty()) {
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
            throws Exception {
        return mWalletApi.transfer(strFrom,  strTo, strAmount, strAssetSymbol, strMemo, amount_to_fee, symbol_to_fee);
    }

    public signed_transaction borrow_asset(String amount_to_borrow,String asset_symbol,String amount_to_collateral,int index) throws Exception {
        return mWalletApi.borrow_asset(amount_to_borrow,asset_symbol,amount_to_collateral,index);
    }

    public signed_transaction change_account_key(String account,String pubkey) throws Exception {
        return mWalletApi.change_account_key(account, pubkey);
    }

    public signed_transaction create_transcation(operations.base_operation base_operation) throws Exception {
        return mWalletApi.create_transcation(base_operation);
    }

    // 获取区块信息
    public block_chain_info info() throws Exception {
        return mWalletApi.get_info();
    }

    //获取块信息
    public block_object get_block(int  nblocknum) throws  Exception {

        return mWalletApi.get_block(nblocknum);
    }

    // 获取对于基础货币的所有市场价格
    public Map<object_id<asset_object>, List<bucket_object>> get_market_histories_base(List<object_id<asset_object>> listAssetObjectId
    ,Integer bugket,Date dateObjectStart, Date dateObjectEnd
    ) throws Exception {
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

            if (!listBucketObject.isEmpty()) {
                //bucket_object bucketObject = listBucketObject.get(listBucketObject.size() - 1);
                mapId2BucketObject.put(objectId, listBucketObject);
            }
        }
        
        return mapId2BucketObject;
    }

    public block_header get_block_header(int nBlockNumber) throws Exception {
        return mWalletApi.get_block_header(nBlockNumber);
    }

    public List<operation_history_object> get_history(boolean bRefresh) throws Exception {
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

    public List<CallOrder> get_call_orders(String assetIdOrName,Integer limit) throws NetworkStatusException {
        object_id object_id = com.github.chouheiwa.wallet.socket.chain.object_id.create_from_string(assetIdOrName);

        if (object_id == null) {
            asset_object list = null;

            try {
                list = lookup_asset_symbols(assetIdOrName);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (list == null) return null;

            object_id = list.id;
        }

        return mWalletApi.get_call_orders(object_id,limit);
    }

    public List<operation_history_object> get_account_history(object_id<account_object> accountObjectId,
                                                              int nLimit,
                                                              boolean bRefresh) throws Exception {
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

    public List<operation_history_object> get_account_history_operations_with_last_id(object_id<account_object> accountId, int operation, int nLimit,int id) throws NetworkStatusException {
        return mWalletApi.get_account_history_operations_with_last_id(accountId, operation, nLimit, id);
    }

    public List<operation_history_object> get_account_history_operations(object_id<account_object> accountId, int operation,int start,int stop,int nLimit) throws NetworkStatusException {
        return mWalletApi.get_account_history_operations(accountId,operation,start,stop,nLimit);
    }

    public operation_types_histoy_object get_account_history_by_operations(String accountIdOrName,List<Integer> operation_types, int start, int nLimit) throws Exception {
        return mWalletApi.get_account_history_by_operations(accountIdOrName,operation_types,start,nLimit);
    }

    public BitshareData prepare_data_to_display(boolean bRefresh,String quotoSymbo,Integer bugket,Date dateStart, Date dateEnd,BitsharesWalletWraper bww) {
        try {
            List<asset> listBalances = bww.list_balances(bRefresh);

            HashSet<object_id<account_object>> hashSetObjectId = new HashSet<object_id<account_object>>();
            HashSet<object_id<asset_object>> hashSetAssetObject = new HashSet<object_id<asset_object>>();
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
;
            mBitshareData.mapId2AccountObject = mapId2AccountObject;
            mBitshareData.mapAssetId2Bucket = mapAssetId2Bucket;

            return mBitshareData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public signed_transaction sell_asset(String amountToSell, String symbolToSell,
                                         String minToReceive, String symbolToReceive,
                                         int timeoutSecs, boolean fillOrKill,String amount_to_fee,String symbol_to_fee,int index)
            throws Exception {
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
            throws Exception {
        return mWalletApi.sell(quote, base, minToReceive, amount,amount_to_fee,symbol_to_fee,index);
    }

    public signed_transaction sell(String base, String quote, double rate, double amount,
                                   int timeoutSecs, String amount_to_fee,
                                   String symbol_to_fee,int index) throws Exception {
        return mWalletApi.sell(base, quote, rate, amount, timeoutSecs,amount_to_fee,symbol_to_fee,index);
    }

    public signed_transaction buy(  String quote, String base , double amountToSell, double amount, String amount_to_fee,
                                  String symbol_to_fee,int index)
            throws Exception {
        return mWalletApi.buy(quote, base, amountToSell, amount,amount_to_fee,symbol_to_fee,index);
    }

    public signed_transaction buy(String base, String quote, double rate, double amount,
                                  int timeoutSecs, String amount_to_fee,
                                  String symbol_to_fee,int index) throws Exception {
        return mWalletApi.buy(base, quote, rate, amount, timeoutSecs,amount_to_fee,symbol_to_fee,index);
    }

    public BitshareData getBitshareData() {
        return mBitshareData;
    }

    public account_object get_account_object(String strAccount) throws Exception {
        return mWalletApi.get_account(strAccount);
    }

    public asset transfer_calculate_fee(String strAmount,
                                        String strAssetSymbol,
                                        String strMemo) throws Exception {
        return mWalletApi.transfer_calculate_fee(strAmount, strAssetSymbol, strMemo);
    }

    public String get_plain_text_message(memo_data memoData) {
        return mWalletApi.decrypt_memo_message(memoData);
    }

    public List<full_account_object> get_full_accounts(List<String> names, boolean subscribe)
            throws Exception {
        return mWalletApi.get_full_accounts(names, subscribe);
    }

    public signed_transaction cancel_order(object_id<limit_order_object> id)
            throws Exception {
        return mWalletApi.cancel_order(id);
    }

    public global_property_object get_global_properties() throws Exception {
        return mWalletApi.get_global_properties();
    }

    public HashMap<types.public_key_type, types.private_key_type> get_wallet_hash() {
        return  mWalletApi.get_wallet_hash();
    }
    //查询矿池总量
    public long get_witness_budget() throws Exception {
        long witness_budget = 0;
        dynamic_global_property_object  dynamic_obj =  mWalletApi.get_dynamic_global_properties();
        if (dynamic_obj == null) {
            return witness_budget;
        } else {
            witness_budget = dynamic_obj.witness_budget;
        }
        return witness_budget;
    }

    public List<account_object> lookup_account_names(String strAccountName) throws Exception {
        return mWalletApi.lookup_account_names(strAccountName);
    }

    public List<operation_history_object> get_account_history_with_last_id(String accountId, int nLimit,String id) throws Exception {
        object_id id_object = object_id.create_from_string(accountId);

        if (id_object == null) {
            id_object = get_account_object(accountId).id;
        }

        return mWalletApi.get_account_history_with_last_id(id_object,nLimit,id);
    }
    /**
     * 24小时成交记录
     * @param base 基础币种
     * @param quote 报价币种
     * @return 市场行情
     * @throws Exception 网络及其他异常
     */
    public MarketTicker get_ticker(String base, String quote) throws Exception {
        return mWalletApi.get_ticker(base, quote);
    }

    public JsonElement get_bitasset_data(String symbol) throws Exception {
        return mWalletApi.get_bitasset_data(symbol);
    }

    public AllHistory get_all_history(String baseSymbolId, String qouteSymbolId, int nLimit) throws Exception {
        return mWalletApi.get_all_history(baseSymbolId, qouteSymbolId,nLimit);
    }

    public OrderBook get_order_book(String base, String quote, int limit) throws  Exception{
        return  mWalletApi.get_order_book(base,quote,limit);
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
