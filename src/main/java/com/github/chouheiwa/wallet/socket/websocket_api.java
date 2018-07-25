package com.github.chouheiwa.wallet.socket;

import com.github.chouheiwa.wallet.socket.websocketClient.websocketClient;
import com.github.chouheiwa.wallet.socket.websocketClient.websocketInterface;
import com.github.chouheiwa.wallet.utils.*;
import com.github.chouheiwa.wallet.socket.chain.*;
import com.github.chouheiwa.wallet.socket.common.ErrorCode;
import com.github.chouheiwa.wallet.socket.exception.NetworkStatusException;
import com.github.chouheiwa.wallet.socket.fc.crypto.sha256_object;
import com.github.chouheiwa.wallet.socket.market.MarketTicker;
import com.github.chouheiwa.wallet.socket.market.OrderBook;
import com.github.chouheiwa.wallet.socket.websocketClient.websocketClient;
import com.github.chouheiwa.wallet.socket.websocketClient.websocketInterface;
import com.google.common.primitives.UnsignedLong;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.github.chouheiwa.wallet.net.model.AllHistory;
import com.github.chouheiwa.wallet.net.model.HistoryResponseModel;
import com.github.chouheiwa.wallet.socket.chain.*;
import com.github.chouheiwa.wallet.socket.exception.NetworkStatusException;
import com.github.chouheiwa.wallet.socket.fc.crypto.sha256_object;
import com.github.chouheiwa.wallet.socket.market.MarketTicker;
import com.github.chouheiwa.wallet.socket.market.OrderBook;
//import okhttp3.*;
import okio.ByteString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.chouheiwa.wallet.socket.common.ErrorCode.ERROR_CONNECT_SERVER_FAILD;

public class websocket_api implements websocketInterface {
    protected  final Logger log = LoggerFactory.getLogger(this.getClass());
    private int _nDatabaseId = -1;
    private int _nHistoryId = -1;
    private int _nBroadcastId = -1;

    private websocketClient mWebsocket;
//    private OkHttpClient mOkHttpClient;
//    private WebSocket mWebsocket;

    private int mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
    private int mnCliConnectStatus = WEBSOCKET_CONNECT_INVALID;
    public static int WEBSOCKET_CONNECT_INVALID = -1;
    private static int WEBSOCKET_CONNECT_SUCCESS = 0;
    private static int WEBSOCKET_ALL_READY = 0;
    private static int WEBSOCKET_CONNECT_FAIL = 1;

    private AtomicInteger mnCallId = new AtomicInteger(1);
    private HashMap<Integer, IReplyObjectProcess> mHashMapIdToProcess = new HashMap<>();
    private String gRespJson;

    @Override
    public void onOpen() {
//        System.out.println("连接成功");
        synchronized (mWebsocket) {
            mnConnectStatus = WEBSOCKET_CONNECT_SUCCESS;
            mWebsocket.notify();
        }
    }

    @Override
    public void onMessage(String resultMsg) {
        try {
            long endTime = System.currentTimeMillis();
            log.info("接收Send请求" + (endTime - startTime));
            Gson gson = new Gson();
            ReplyBase replyObjectBase = gson.fromJson(resultMsg, ReplyBase.class);

            IReplyObjectProcess iReplyObjectProcess = null;
            synchronized (mHashMapIdToProcess) {
                if (mHashMapIdToProcess.containsKey(replyObjectBase.id)) {
                    iReplyObjectProcess = mHashMapIdToProcess.get(replyObjectBase.id);
                    log.info("接收Send请求  mapid=" + replyObjectBase.id);
                }
            }

            if (iReplyObjectProcess != null) {
                iReplyObjectProcess.processTextToObject(resultMsg+"\t\n");

                iReplyObjectProcess.notify();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof IOException) {  // 出现io错误
            if (mWebsocket != null){
                synchronized (mWebsocket) {
                    mnConnectStatus = WEBSOCKET_CONNECT_FAIL;
                    mWebsocket.notify();
                }
            }
        }
    }

    class WebsocketError {
        int code;
        String message;
        Object data;
    }

    class Call {
        int id;
        String method;
        List<Object> params;
    }

    class Reply<T> {
        String id;
        String jsonrpc;
        T result;
        WebsocketError error;
    }

    class ReplyBase {
        int id;
        String jsonrpc;
    }

    private interface IReplyObjectProcess<T> {
        void processTextToObject(String strText);
        T getReplyObject();
        String getError();
        void notifyFailure(Throwable t);
        Throwable getException();
        String getResponse();
    }

    private class ReplyObjectProcess<T> implements IReplyObjectProcess<T> {
        private String strError;
        private T mT;
        private Type mType;
        private Throwable exception;
        private String strResponse;
        public ReplyObjectProcess(Type type) {
            mType = type;
        }

        public void processTextToObject(String strText) {
            try {
                strResponse = strText;
                gRespJson = strText;
                Gson gson = global_config_object.getInstance().getGsonBuilder().create();
                mT = gson.fromJson(strText, mType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
            } catch (Exception e) {
                e.printStackTrace();
                strError = e.getMessage();
                strResponse = strText;
            }
            synchronized (this) {
                notify();
            }
        }

        @Override
        public T getReplyObject() {
            return mT;
        }

        @Override
        public String getError() {
            return strError;
        }

        @Override
        public void notifyFailure(Throwable t) {
            exception = t;
            synchronized (this) {
                notify();
            }
        }

        @Override
        public Throwable getException() {
            return exception;
        }

        @Override
        public String getResponse() {
            return strResponse;
        }
    }

    public synchronized int connect(String ws_server) {
        if (mnConnectStatus == WEBSOCKET_ALL_READY) {
            return 0;
        }
        String strServer = ws_server;
        if (TextUtils.isEmpty(strServer)) {
            return ErrorCode.ERROR_CONNECT_SERVER_FAILD;
        }

        try {
            mWebsocket = new websocketClient(strServer,this);
            mWebsocket.connect();
//            System.out.println("开始连接" + strServer);
        } catch (URISyntaxException e) {
            return ErrorCode.ERROR_CONNECT_SERVER_FAILD;
        }
        synchronized (mWebsocket) {
            if (mnConnectStatus == WEBSOCKET_CONNECT_INVALID) {
                try {
                    mWebsocket.wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mnConnectStatus != WEBSOCKET_CONNECT_SUCCESS) {
//                    System.out.println("连接失败" + strServer);
                    return ErrorCode.ERROR_CONNECT_SERVER_FAILD;
                }
            }
        }

        int nRet = 0;
        try {
            boolean bLogin = login("", "");
            if (bLogin == true) {
                _nDatabaseId = get_websocket_bitshares_api_id("database");
                _nHistoryId = get_websocket_bitshares_api_id("history");
                _nBroadcastId = get_websocket_bitshares_api_id("network_broadcast");
            } else {
                nRet = ErrorCode.ERROR_CONNECT_SERVER_FAILD;
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
            nRet = ErrorCode.ERROR_CONNECT_SERVER_FAILD;
        }

        if (nRet != 0) {
            mWebsocket.close(1000, "");
            mWebsocket = null;
            mnConnectStatus = WEBSOCKET_CONNECT_INVALID;
        } else {
            mnConnectStatus = WEBSOCKET_ALL_READY;
        }

        return nRet;
    }

    public synchronized int close() {
        mHashMapIdToProcess.clear();

        if (mWebsocket != null){
            mWebsocket.close(1000, "Close");
        }
        mWebsocket = null;
        mnConnectStatus = WEBSOCKET_CONNECT_INVALID;

        _nDatabaseId = -1;
        _nBroadcastId = -1;
        _nHistoryId = -1;
        return 0;
    }

    private boolean login(String strUserName, String strPassword) throws NetworkStatusException {
        Call callObject = new Call();

        callObject.id = mnCallId.getAndIncrement();;
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(1);
        callObject.params.add("login");

        List<Object> listLoginParams = new ArrayList<>();
        listLoginParams.add(strUserName);
        listLoginParams.add(strPassword);
        callObject.params.add(listLoginParams);

        ReplyObjectProcess<Reply<Boolean>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<Boolean>>(){}.getType());
        Reply<Boolean> replyLogin = sendForReplyImpl(callObject, replyObject);
        if (replyLogin == null) {
            return false;
        } else {
            return replyLogin.result;
        }
    }

    private int get_websocket_bitshares_api_id(String strApiName) throws NetworkStatusException {
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(1);
        callObject.params.add(strApiName);

        List<Object> listDatabaseParams = new ArrayList<>();
        callObject.params.add(listDatabaseParams);

        ReplyObjectProcess<Reply<Integer>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<Integer>>(){}.getType());
        Reply<Integer> replyApiId = sendForReplyImpl(callObject, replyObject);
        if (replyApiId == null) {
            return -1;
        } else {
            return replyApiId.result;
        }
    }

    private int get_database_api_id() throws NetworkStatusException {
        if (_nDatabaseId != -1) return _nDatabaseId;
        int id = get_websocket_bitshares_api_id("database");
        if (-1 != id) {
            _nDatabaseId = id;
        } else {
            close();
            connect(mWebsocket.getURI().toString());
            _nDatabaseId = get_websocket_bitshares_api_id("database");
        }
        return _nDatabaseId;
    }

    private int get_history_api_id() throws NetworkStatusException {
        if (_nHistoryId != -1) return _nHistoryId;
        int id = get_websocket_bitshares_api_id("history");
        if (-1 != id) {
            _nHistoryId = id;
        }else {
            close();
            connect(mWebsocket.getURI().toString());
            _nHistoryId = get_websocket_bitshares_api_id("history");
        }
        return _nHistoryId;
    }

    private int get_broadcast_api_id() throws NetworkStatusException {
        if (_nBroadcastId != -1) return _nBroadcastId;
        int id = get_websocket_bitshares_api_id("network_broadcast");
        if ( -1 != id) {
            _nBroadcastId = id;
        }else {
            close();
            connect(mWebsocket.getURI().toString());
            _nBroadcastId = get_websocket_bitshares_api_id("network_broadcast");
        }
        return _nBroadcastId;
    }

    public sha256_object get_chain_id() throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_chain_id");

        List<Object> listDatabaseParams = new ArrayList<>();

        callObject.params.add(listDatabaseParams);

        ReplyObjectProcess<Reply<sha256_object>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<sha256_object>>(){}.getType());
        Reply<sha256_object> replyDatabase = sendForReply(callObject, replyObject);
        if (replyDatabase == null) {
            return null;
        } else {
            return replyDatabase.result;
        }
    }

    public List<CallOrder> get_call_orders(object_id asset_id, Integer limit) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_call_orders");

        List<Object> listDatabaseParams = new ArrayList<>();

        listDatabaseParams.add(asset_id);

        listDatabaseParams.add(limit);

        callObject.params.add(listDatabaseParams);

        ReplyObjectProcess<Reply<List<CallOrder>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<CallOrder>>>(){}.getType());
        Reply<List<CallOrder>> replyDatabase = sendForReply(callObject, replyObject);

//        System.out.println(replyObject.getResponse());

        if (replyDatabase == null) {
            return null;
        } else {
            return replyDatabase.result;
        }
    }

    public List<account_object> lookup_account_names(String strAccountName) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("lookup_account_names");

        List<Object> listAccountNames = new ArrayList<>();
        listAccountNames.add(strAccountName);

        List<Object> listAccountNamesParams = new ArrayList<>();
        listAccountNamesParams.add(listAccountNames);

        callObject.params.add(listAccountNamesParams);

        ReplyObjectProcess<Reply<List<account_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<account_object>>>(){}.getType());
        Reply<List<account_object>> replyAccountObjectList = sendForReply(callObject, replyObject);
        if (replyAccountObjectList == null) {
            return null;
        } else {
            return replyAccountObjectList.result;
        }
    }

    public List<account_object> get_accounts(List<object_id<account_object>> listAccountObjectId) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_accounts");

        List<Object> listAccountIds = new ArrayList<>();
        listAccountIds.add(listAccountObjectId);

        List<Object> listAccountNamesParams = new ArrayList<>();
        listAccountNamesParams.add(listAccountIds);

        callObject.params.add(listAccountIds);
        ReplyObjectProcess<Reply<List<account_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<account_object>>>(){}.getType());
        Reply<List<account_object>> replyAccountObjectList = sendForReply(callObject, replyObject);
        if (replyAccountObjectList == null) {
            List<account_object> Obj = new ArrayList<account_object>();
            return Obj;
        } else {
            return replyAccountObjectList.result;
        }
    }

    public List<asset> list_account_balances(object_id<account_object> accountId) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_account_balances");

        List<Object> listAccountBalancesParam = new ArrayList<>();
        listAccountBalancesParam.add(accountId);
        listAccountBalancesParam.add(new ArrayList<Object>());
        callObject.params.add(listAccountBalancesParam);

        ReplyObjectProcess<Reply<List<asset>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset>>>(){}.getType());
        Reply<List<asset>> replyLookupAccountNames = sendForReply(callObject, replyObject);
        if (replyLookupAccountNames == null) {
            List<asset> Obj = new ArrayList<asset>();
            return Obj;
        } else {
            return replyLookupAccountNames.result;
        }
    }

    public List<operation_history_object> get_account_history(object_id<account_object> accountId, int nLimit) throws NetworkStatusException {
        _nHistoryId = get_history_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_account_history");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add("1.11.0");
        listAccountHistoryParam.add(nLimit);
        listAccountHistoryParam.add("1.11.0");
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>(){}.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);
        if (replyAccountHistory == null) {
            return null;
        } else {
            return replyAccountHistory.result;
        }
    }

    public operation_types_histoy_object get_account_history_by_operations(object_id<account_object> accountId, List<Integer> operation_types, int start, int nLimit) throws NetworkStatusException {
        _nHistoryId = get_history_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_account_history_by_operations");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add(operation_types);
        listAccountHistoryParam.add(start);
        listAccountHistoryParam.add(nLimit);
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<operation_types_histoy_object>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<operation_types_histoy_object>>(){}.getType());
        Reply<operation_types_histoy_object> replyAccountHistory = sendForReply(callObject, replyObject);
        if (replyAccountHistory == null) {
            return null;
        } else {
            return replyAccountHistory.result;
        }
    }

    public List<operation_history_object> get_account_history_operations(object_id<account_object> accountId, int operation_id, int start, int stop, int nLimit) throws NetworkStatusException {
        _nHistoryId = get_history_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_account_history_operations");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add(operation_id);
        listAccountHistoryParam.add("1.11." + start);
        listAccountHistoryParam.add("1.11." + stop);
        listAccountHistoryParam.add(nLimit);
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>(){}.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);
        if (replyAccountHistory == null) {
            return null;
        } else {
            return replyAccountHistory.result;
        }
    }


    public List<operation_history_object> get_account_history_with_last_id(object_id<account_object> accountId, int nLimit, String id) throws NetworkStatusException {
        _nHistoryId = get_history_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_account_history");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add("1.11.0");
        listAccountHistoryParam.add(nLimit);
        listAccountHistoryParam.add(id);
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>(){}.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);

        if (replyAccountHistory == null) return null;

        return replyAccountHistory.result;
    }

    public account_object get_account(String strAccountNameOrId) throws NetworkStatusException {
        object_id<account_object> accountObjectId = object_id.create_from_string(strAccountNameOrId);

        List<account_object> listAccountObject;
        if (accountObjectId == null) {
            listAccountObject = lookup_account_names(strAccountNameOrId);

            if (listAccountObject == null) {
                return null;
            } else {
                if (listAccountObject.isEmpty()) {
                    return null;
                } else {
                    if (listAccountObject.size() > 0) {
                        return listAccountObject.get(0);
                    } else {
                        return null;
                    }
                }
            }
        } else {
            List<object_id<account_object>> listAccountObjectId = new ArrayList<>();
            listAccountObjectId.add(accountObjectId);
            listAccountObject = get_accounts(listAccountObjectId);
            if (listAccountObject == null) {
                return null;
            } else {
                if (listAccountObject.isEmpty()) {
                    return null;
                } else {
                    if (listAccountObject.size() >0) {
                        return listAccountObject.get(0);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public account_object get_witness(String strAccountNameOrId) throws NetworkStatusException {
        object_id<account_object> accountObjectId = object_id.create_from_string(strAccountNameOrId);
        List<account_object> listAccountObject;
        if (accountObjectId == null) {
            listAccountObject = lookup_account_names(strAccountNameOrId);

            if (listAccountObject == null) {
                return null;
            } else {
                if (listAccountObject.isEmpty()) {
                    return null;
                } else {
                    if (listAccountObject.size() > 0) {
                        return listAccountObject.get(0);
                    } else {
                        return null;
                    }
                }
            }
        } else {
            List<object_id<account_object>> listAccountObjectId = new ArrayList<>();
            listAccountObjectId.add(accountObjectId);
            listAccountObject = get_accounts(listAccountObjectId);
            if (listAccountObject == null) {
                return null;
            } else {
                if (listAccountObject.isEmpty()) {
                    return null;
                } else {
                    if (listAccountObject.size() >0) {
                        return listAccountObject.get(0);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    //查询转账交易列表
    public List<HistoryResponseModel.DataBean> get_transfer_history(object_id<account_object> accountId, int nLimit) throws NetworkStatusException, JSONException,Exception {
        _nHistoryId = get_history_api_id();
        BitsharesWalletWraper bww = new BitsharesWalletWraper();
        List<asset_object> objAssets = bww.list_assets_obj("", 100);
        if (objAssets == null) {
            return null;
        }
        List<HistoryResponseModel.DataBean> list = new ArrayList<HistoryResponseModel.DataBean>();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_account_history");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add("1.11.0");
        listAccountHistoryParam.add(nLimit);
        listAccountHistoryParam.add("1.11.0");
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>(){}.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);
        if (replyAccountHistory == null) {
            List<HistoryResponseModel.DataBean> Obj = new ArrayList<HistoryResponseModel.DataBean>();
            return Obj;
        } else {
            String jsonRpc = replyObject.getResponse();
            try {
                JSONObject jsonObject = new JSONObject(jsonRpc);
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                HistoryResponseModel.DataBean dataBean = null;
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    JSONArray jsonArray1 = jsonObject1.optJSONArray("op");

                    if (jsonArray1.getString(0).equalsIgnoreCase("0")){
                        dataBean = new HistoryResponseModel.DataBean();
                        String block_order_id = jsonObject1.optString("id");
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(1);
                        String fromId = jsonObject2.optString("from");
                        String toId = jsonObject2.optString("to");
                        String amount = jsonObject2.optJSONObject("amount").optString("amount");
                        String type =  jsonObject2.optJSONObject("amount").optString("asset_id");
                        dataBean.setId(block_order_id);
                        for (asset_object objAsset : objAssets){
                            if (objAsset.id.toString().equalsIgnoreCase(type)){
                                dataBean.setSymbol(objAsset.symbol);
                                asset_object.asset_object_legible myasset = objAsset.get_legible_asset_object(Long.parseLong(amount));
                                dataBean.setAmount(NumberUtils.formatNumber(myasset.count));
                                String sFromName =  get_account(fromId).name;
                                String sToName = get_account(toId).name;
                                dataBean.setFrom(sFromName);
                                dataBean.setTo(sToName);
                                list.add(dataBean);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return list;
            }
            return list;
        }
    }

    //查询转账交易列表
    public List<HistoryResponseModel.DataBean> get_transfer_history_by_flag(object_id<account_object> accountId, int nLimit,String lastBlockIndex) throws NetworkStatusException, JSONException,Exception {
        _nHistoryId = get_history_api_id();
        BitsharesWalletWraper bww = new BitsharesWalletWraper();
        List<asset_object> objAssets = bww.list_assets_obj("", 100);
        if (objAssets == null) {
            return null;
        }
        List<HistoryResponseModel.DataBean> list = new ArrayList<HistoryResponseModel.DataBean>();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_account_history");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(accountId);
        listAccountHistoryParam.add("1.11.0");
        listAccountHistoryParam.add(nLimit);
        if(null == lastBlockIndex || "".equals(lastBlockIndex)){
            listAccountHistoryParam.add("1.11.0");
        }else{
            listAccountHistoryParam.add(lastBlockIndex);
        }
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<List<operation_history_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<operation_history_object>>>(){}.getType());
        Reply<List<operation_history_object>> replyAccountHistory = sendForReply(callObject, replyObject);
        if (replyAccountHistory == null) {
            List<HistoryResponseModel.DataBean> Obj = new ArrayList<HistoryResponseModel.DataBean>();
            return Obj;
        } else {
            String jsonRpc = replyObject.getResponse();
            try {
                JSONObject jsonObject = new JSONObject(jsonRpc);
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                HistoryResponseModel.DataBean dataBean = null;
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    JSONArray jsonArray1 = jsonObject1.optJSONArray("op");
                    String block_order_id = jsonObject1.optString("id");
                    if (jsonArray1.getString(0).equalsIgnoreCase("0")){
                        dataBean = new HistoryResponseModel.DataBean();
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(1);
                        String fromId = jsonObject2.optString("from");
                        String toId = jsonObject2.optString("to");
                        String amount = jsonObject2.optJSONObject("amount").optString("amount");
                        String type =  jsonObject2.optJSONObject("amount").optString("asset_id");
                        String memo = jsonObject2.optString("memo");
                        dataBean.setId(block_order_id);
                        dataBean.setMemo(memo);
                        for (asset_object objAsset : objAssets){
                            if (objAsset.id.toString().equalsIgnoreCase(type)){
                                dataBean.setSymbol(objAsset.symbol);
                                asset_object.asset_object_legible myasset = objAsset.get_legible_asset_object(Long.parseLong(amount));
                                dataBean.setAmount(NumberUtils.formatNumber(myasset.count));
                                String sFromName =  get_account(fromId).name;
                                String sToName = get_account(toId).name;
                                dataBean.setFrom(sFromName);
                                dataBean.setTo(sToName);
                                list.add(dataBean);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return list;
            }
            return list;
        }
    }

    public global_property_object get_global_properties() throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_global_properties");
        callObject.params.add(new ArrayList<>());

        ReplyObjectProcess<Reply<global_property_object>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<global_property_object>>(){}.getType());
        Reply<global_property_object> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            return null;
        } else {
            return replyObject.result;
        }
    }

    public dynamic_global_property_object get_dynamic_global_properties() throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_dynamic_global_properties");
        callObject.params.add(new ArrayList<Object>());
        ReplyObjectProcess<Reply<dynamic_global_property_object>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<dynamic_global_property_object>>(){}.getType());
        Reply<dynamic_global_property_object> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            return null;
        } else {
            return replyObject.result;
        }
    }

    public List<asset_object> list_assets(String strLowerBound, int nLimit) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();

        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("list_assets");

        List<Object> listAssetsParam = new ArrayList<>();
        listAssetsParam.add(strLowerBound);
        listAssetsParam.add(nLimit);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset_object>>>(){}.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            List<asset_object> Obj = new ArrayList<asset_object>();
            return Obj;
        } else {
            return replyObject.result;
        }
    }

    public List<asset_object> get_assets(List<object_id<asset_object>> listAssetObjectId) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_assets");

        List<Object> listAssetsParam = new ArrayList<>();

        List<Object> listObjectId = new ArrayList<>();
        listObjectId.addAll(listAssetObjectId);

        listAssetsParam.add(listObjectId);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset_object>>>(){}.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            List<asset_object> Obj = new ArrayList<asset_object>();
            return Obj;
        } else {
            return replyObject.result;
        }
    }

    public String lookup_asset_symbols_rate(String strAssetSymbol) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        String result = "";
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("lookup_asset_symbols");
        List<Object> listAssetsParam = new ArrayList<>();
        List<Object> listAssetSysmbols = new ArrayList<>();
        listAssetSysmbols.add(strAssetSymbol);
        listAssetsParam.add(listAssetSysmbols);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset_object>>>(){}.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            return "0";
        } else {
            String jsonRpc = replyObjectProcess.getResponse();
            double nRate = 0.0;
            JSONObject jsonobj = null;
            JSONArray jarray = null;
            JSONObject job = null;
            try {
                jsonobj = new JSONObject(jsonRpc);
                jarray = jsonobj.getJSONArray("result");
                job = jarray.getJSONObject(0);
                String asset_cur_id = job.getString("id");
                int precision = job.getInt("precision");
                double base_amount = job.getJSONObject("options").getJSONObject("core_exchange_rate").getJSONObject("base").getDouble("amount");
                String base_asset_id = job.getJSONObject("options").getJSONObject("core_exchange_rate").getJSONObject("base").getString("asset_id");
                double quote_amount = job.getJSONObject("options").getJSONObject("core_exchange_rate").getJSONObject("quote").getDouble("amount");
                String quote_asset_id = job.getJSONObject("options").getJSONObject("core_exchange_rate").getJSONObject("quote").getString("asset_id");

                int targetPrec = 1;
                int bdsPrec = 1;
                //cacle other symbol precision
                if (precision <= 1){
                    return "0";
                }
                for (int i = 0; i < precision; i++) {
                    targetPrec *= 10;
                }
                //cacle bds symbol precision
                for (int n = 0; n < 5; n++) {
                    bdsPrec *= 10;
                }
                //cacle base and quote precent
                if (base_asset_id.equals("1.3.0")) {
                    //base is BDS
                    nRate = (quote_amount / targetPrec) / (base_amount / bdsPrec);
                } else if (quote_asset_id.equals("1.3.0")) {
                    //quote is BDS
                    nRate = (base_amount / targetPrec) / (quote_amount / bdsPrec);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return "0";
            }
            result = nRate + "";
            return result;
        }
    }

    public asset_object lookup_asset_symbols(String strAssetSymbol) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("lookup_asset_symbols");

        List<Object> listAssetsParam = new ArrayList<>();

        List<Object> listAssetSymbols = new ArrayList<>();
        listAssetSymbols.add(strAssetSymbol);

        listAssetsParam.add(listAssetSymbols);
        callObject.params.add(listAssetsParam);

        ReplyObjectProcess<Reply<List<asset_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<asset_object>>>(){}.getType());
        Reply<List<asset_object>> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            return null;
        } else {
            return replyObject.result.get(0);
        }
    }

    public block_header get_block_header(int nBlockNumber) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_block_header");
        List<Object> listBlockNumber = new ArrayList<>();
        listBlockNumber.add(nBlockNumber);
        callObject.params.add(listBlockNumber);

        ReplyObjectProcess<Reply<block_header>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<block_header>>(){}.getType());
        Reply<block_header> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            block_header Obj = new block_header();
            return Obj;
        } else {
            return replyObject.result;
        }
    }
    //查询区块信息
    public block_object get_block(int nBlockNumber) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        block_object block = new block_object();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_block");
        List<Object> listBlockNumber = new ArrayList<>();
        listBlockNumber.add(nBlockNumber);
        callObject.params.add(listBlockNumber);

        ReplyObjectProcess<Reply<block_header>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<block_header>>(){}.getType());
        Reply<block_header> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            block_object obj = new block_object();
            return obj;
        } else {
            String sBlock = replyObjectProcess.getResponse();
            if (sBlock != null) {
                try {
                    JSONObject jsonobj = new JSONObject(sBlock);
                    JSONObject dataobj = jsonobj.getJSONObject("result");
                    block.blockNumber = nBlockNumber + "";
                    String date = TimeUtils.utc2Local(dataobj.getString("timestamp").replace("T"," "));
                    block.timeStame = date;

                    block.witnessId = dataobj.getString("witness");
                    JSONArray jarray = dataobj.getJSONArray("transactions");
                    block.transactionCount = jarray.length();
                    block.transaction_ids = new ArrayList<>();
                    for (int i =0; i < jarray.length();i ++) {
                        String json =  jarray.getString(i);
                        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
                        transaction trans = gson.fromJson(json,transaction.class);

                        block.transaction_ids.add(trans.ids().toString().substring(0,40));
                    }
                    return block;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public int broadcast_transaction(signed_transaction tx) throws NetworkStatusException {
        _nBroadcastId = get_broadcast_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nBroadcastId);
        callObject.params.add("broadcast_transaction");
        List<Object> listTransaction = new ArrayList<>();
        listTransaction.add(tx);
        callObject.params.add(listTransaction);

        ReplyObjectProcess<Reply<Object>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<Integer>>(){}.getType());
        Reply<Object> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            //return -1;
            return -2;
        } else {
            if (replyObject.error != null) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public int broadcast_json_transaction(signed_transaction tx, String type) throws NetworkStatusException {
        _nBroadcastId = get_broadcast_api_id(); //没有调用
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nBroadcastId);
        callObject.params.add("broadcast_transaction");
        List<Object> listTransaction = new ArrayList<>();
        listTransaction.add(tx);
        callObject.params.add(listTransaction);

        ReplyObjectProcess<Reply<Object>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<Integer>>(){}.getType());

        Reply<Object> replyObject = sendJsonForReplyImpl(callObject,replyObjectProcess);
        if (replyObject == null) {
            return -1;
        } else {
            if (replyObject.error != null) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public List<bucket_object> get_market_history(object_id<asset_object> assetObjectId1,
                                                  object_id<asset_object> assetObjectId2,
                                                  int nBucket,
                                                  Date dateStart,
                                                  Date dateEnd) throws NetworkStatusException {
        _nHistoryId = get_history_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_market_history");

        List<Object> listParams = new ArrayList<>();
        listParams.add(assetObjectId1);
        listParams.add(assetObjectId2);
        listParams.add(nBucket);
        listParams.add(dateStart);
        listParams.add(dateEnd);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<bucket_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<bucket_object>>>(){}.getType());
        Reply<List<bucket_object>> replyObject = sendForReply(callObject, replyObjectProcess);
        if (replyObject == null) {
            List<bucket_object> Obj = new ArrayList<bucket_object>();
            return Obj;
        } else {
            return replyObject.result;
        }
    }

    public List<limit_order_object> get_limit_orders(List<object_id<limit_order_object>> ids)
            throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_objects");

        List<Object> listParams = new ArrayList<>();
        listParams.add(ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>(){}.getType());
        Reply<List<limit_order_object>> reply = sendForReply(callObject, replyObject);
        if (reply == null) {
            return null;

        } else {
            return reply.result;
        }
    }

    public List<limit_order_object> get_limit_orders(object_id<asset_object> base,
                                                     object_id<asset_object> quote,
                                                     int limit) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_limit_orders");

        List<Object> listParams = new ArrayList<>();
        listParams.add(base);
        listParams.add(quote);
        listParams.add(limit);
        callObject.params.add(listParams);
        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>(){}.getType());
        Reply<List<limit_order_object>> replyObject = sendForReply(callObject, replyObjectProcess);

        if (replyObject == null) {
            List<limit_order_object> Obj = new ArrayList<limit_order_object>();
            return Obj;

        } else {
            return replyObject.result;
        }
    }

    public void subscribe_to_market(object_id<asset_object> base, object_id<asset_object> quote) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("subscribe_to_market");

        List<Object> listParams = new ArrayList<>();
        listParams.add(callObject.id);
        listParams.add(base);
        listParams.add(quote);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<String>> replyObject = new ReplyObjectProcess<>(new TypeToken<Reply<String>>(){}.getType());
        sendForReplyImpl(callObject, replyObject);
    }

    public List<full_account_object> get_full_accounts(List<String> names, boolean subscribe)
            throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_full_accounts");

        List<Object> listParams = new ArrayList<>();
        listParams.add(names);
        listParams.add(subscribe);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<full_account_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<full_account_object>>>(){}.getType());
        Reply<List<full_account_object>> reply = sendForReply(callObject, replyObject);
        if (reply == null) {
            return null;
        } else {
            return reply.result;
        }
    }

    //获取手续费
    public String get_Fee(String id, int op)  throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        String sFee = "0";
        String strResopnse = null;
        object_id<asset_object> ObjectId = object_id.create_from_string(id);
        List<object_id<asset_object>> ids = new ArrayList<>();
        ids.add(ObjectId);

        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_objects");

        List<Object> listParams = new ArrayList<>();
        listParams.add(ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>(){}.getType());
        Reply<List<limit_order_object>> obj = sendForReply(callObject, replyObject);
        if (obj == null ) {
            strResopnse = replyObject.getResponse();
        } else {
            strResopnse = replyObject.getResponse();
            if (strResopnse != null && !strResopnse.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(strResopnse);
                    JSONArray jArrayResult = jsonObject.getJSONArray("result");
                    if (jArrayResult != null && jArrayResult.length() > 0) {
                        JSONObject jsonObject1 = jArrayResult.getJSONObject(0);
                        JSONArray jArrayParams = jsonObject1.getJSONObject("parameters").getJSONObject("current_fees").getJSONArray("parameters");
                        if (jArrayParams != null && jArrayParams.length() > 0) {

                            if (op < jArrayParams.length()) {
                                JSONArray jArrayFees = jArrayParams.getJSONArray(op);
                                if (jArrayFees.getInt(0) == op) {
                                    JSONObject fee_obj = jArrayFees.getJSONObject(1);
                                    switch (op) {
                                        case operations.ID_TRANSER_OPERATION:
                                            long transfer_fee =  fee_obj.getLong("fee");
                                            if (transfer_fee > 0) {
                                                sFee = CalculateUtils.div(Double.parseDouble(transfer_fee+""),100000,5);
                                            }
                                            break;
                                        case operations.ID_UPGRADE_ACCOUNT_OPERATION:
                                            long liftetime_fee = fee_obj.getLong("membership_lifetime_fee");
                                            if (liftetime_fee > 0) {
                                                sFee = CalculateUtils.div(Double.parseDouble(liftetime_fee+""),100000,5);
                                            }
                                            break;
                                        case operations.ID_ACCOUNT_WHITELIST_OPERATION:
                                            long fee = fee_obj.getLong("membership_lifetime_fee");
                                            if (fee > 0) {
                                                sFee = CalculateUtils.div(Double.parseDouble(fee+""),100000,5);
                                            }
                                            break;
                                        case operations.ID_UPDATE_LMMIT_ORDER_OPERATION:
                                        case operations.ID_VESTING_WITHDRAW_OPERATION:
                                            long vest_fee =  fee_obj.getLong("fee");
                                            if (vest_fee > 0) {
                                                sFee = CalculateUtils.div(Double.parseDouble(vest_fee+""),100000,5);
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "0";
                }
            }
        }
        return sFee;
    }

    public String get_object(String id)  throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        String strResopnse = null;
        object_id ObjectId = object_id.create_from_string(id);
        List<object_id> ids = new ArrayList<>();
        ids.add(ObjectId);

        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_objects");

        List<Object> listParams = new ArrayList<>();
        listParams.add(ids);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>(){}.getType());
        Reply<List<limit_order_object>> obj = sendForReply(callObject, replyObject);
        strResopnse = replyObject.getResponse();
        try {
            JSONObject jsonObject = new JSONObject(strResopnse);
            JSONArray jArrayResult = jsonObject.getJSONArray("result");
            return jArrayResult.get(0).toString();
        } catch (JSONException e) {
            return null;
        }

    }


    public limit_order_object get_limit_order(object_id<limit_order_object> id) throws NetworkStatusException {
        return get_limit_orders(Collections.singletonList(id)).get(0);
    }

    private <T> String sendForJsonReply(Call callObject, ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        if (mWebsocket == null || mnConnectStatus != WEBSOCKET_CONNECT_SUCCESS) {
            int nRet = connect(mWebsocket.getURI().toString());
            if (nRet == -1) {
                return null;
            }
        }
        return sendForJsonReplyImpl(callObject, replyObjectProcess);
    }

    private <T> Reply<T> sendForReply(Call callObject, ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        int id = get_websocket_bitshares_api_id("database");
        if (-1 == id) {
            int nRet = connect(mWebsocket.getURI().toString());
            if (nRet == -1) {
                return null;
            }
        }
        if (mWebsocket == null || mnConnectStatus != WEBSOCKET_CONNECT_SUCCESS) {
            int nRet = connect(mWebsocket.getURI().toString());
            if (nRet == -1) {
                //throw new NetworkStatusException("It doesn't connect to the server.");
                return null;
            }
        }

        return sendForReplyImpl(callObject, replyObjectProcess);
    }

    private <T> Reply<T> sendJsonForReplyImpl(Call callObject,
                                              ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        String strMessage = gson.toJson(callObject);

        log.debug("Wallet send message:" + strMessage);

        synchronized (mHashMapIdToProcess) {
            mHashMapIdToProcess.put(callObject.id, replyObjectProcess);
        }
        synchronized (replyObjectProcess) {
            mWebsocket.send(strMessage);

            try {
                replyObjectProcess.wait(2000);
                Reply<T> replyObject = replyObjectProcess.getReplyObject();
                String jsonResp = replyObjectProcess.getResponse();
                String strError = replyObjectProcess.getError();
                if (TextUtils.isEmpty(strError) == false) {
                    throw new NetworkStatusException(strError);
                } else if (replyObjectProcess.getException() != null) {
                    throw new NetworkStatusException(replyObjectProcess.getException());
                } else if (replyObject == null) {
                    throw new NetworkStatusException("Reply object is null.\n" + replyObjectProcess.getResponse());
                }else if (replyObject.error != null) {
                    throw new NetworkStatusException(gson.toJson(replyObject.error));
                }
                return replyObject;
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("Websocket Interrupted:"+e.getMessage());
                return null;
            }
        }
    }

    public <T> String sendJsonForJsonReply(String strMessage, ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        synchronized (mHashMapIdToProcess) {
            mHashMapIdToProcess.put( mnCallId.getAndIncrement(), replyObjectProcess);
        }
        synchronized (replyObjectProcess) {
            mWebsocket.send(strMessage);
            try {
                replyObjectProcess.wait(2000);
                String jsonResp = replyObjectProcess.getResponse();
                String strError = replyObjectProcess.getError();
                if (TextUtils.isEmpty(strError) == false) {
                    throw new NetworkStatusException(strError);
                } else if (replyObjectProcess.getException() != null) {
                    throw new NetworkStatusException(replyObjectProcess.getException());
                }
                return jsonResp;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private <T> String sendForJsonReplyImpl(Call callObject, ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        String strMessage = gson.toJson(callObject);

        synchronized (mHashMapIdToProcess) {
            mHashMapIdToProcess.put(callObject.id, replyObjectProcess);
        }

        synchronized (replyObjectProcess) {
            mWebsocket.send(strMessage);

            try {
                replyObjectProcess.wait(2000);
                String jsonResp = replyObjectProcess.getResponse();
                String strError = replyObjectProcess.getError();
                if (TextUtils.isEmpty(strError) == false) {
                    throw new NetworkStatusException(strError);
                } else if (replyObjectProcess.getException() != null) {
                    throw new NetworkStatusException(replyObjectProcess.getException());
                }
                return jsonResp;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    long startTime;
    private <T> Reply<T> sendForReplyImpl(Call callObject, ReplyObjectProcess<Reply<T>> replyObjectProcess) throws NetworkStatusException {
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();
        String strMessage = gson.toJson(callObject);

        synchronized (mHashMapIdToProcess) {
            mHashMapIdToProcess.put(callObject.id, replyObjectProcess);
        }

        if (mWebsocket == null) {
            connect(mWebsocket.getURI().toString());
            if (mWebsocket == null) {
                //TODO no network
                return null;
            }
        }

        synchronized (replyObjectProcess) {
            log.debug("Wallet send message:" + strMessage);
            mWebsocket.send(strMessage);

            try {
                replyObjectProcess.wait(2000);
                Reply<T> replyObject = replyObjectProcess.getReplyObject();
                String jsonResp = replyObjectProcess.getResponse();

                log.debug("Wallet receive message:" + jsonResp);
                System.out.println("Wallet receive message:" + jsonResp);
                String strError = replyObjectProcess.getError();
                if (TextUtils.isEmpty(strError) == false) {
//                   throw new NetworkStatusException(strError);
                    return null;
                } else if (replyObjectProcess.getException() != null) {
//                   throw new NetworkStatusException(replyObjectProcess.getException());
                    return null;
                } else if (replyObject == null) {
//                    throw new NetworkStatusException("Reply object is null.\n" + replyObjectProcess.getResponse());
                    return null;
                }else if (replyObject.error != null) {
//                   throw new NetworkStatusException(gson.toJson(replyObject.error));
                    return null;
                }
                return replyObject;
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("callObject = [" + callObject + "], replyObjectProcess = [" + replyObjectProcess + "]"+e.getMessage());
                return null;
            }
        }
    }

    public boolean is_public_key_registered(String pub_key) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        boolean isRegistered = false;
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("is_public_key_registered");

        List<Object> listParams = new ArrayList<>();
        listParams.add(pub_key);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>(){}.getType());
        String strResp = null;
        try {
            strResp = sendForJsonReply(callObject, replyObject);
        } catch (Exception e) {
            strResp = replyObject.getResponse();
        }
        if (strResp == null)
            strResp = replyObject.getResponse();

        if (strResp != null && !strResp.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(strResp);
                String strResult = jsonObject.getString("result");
                if (strResult.equals("false"))
                    isRegistered = false;
                else
                    isRegistered = true;
            } catch (JSONException e) {
                e.printStackTrace();
                isRegistered = false;
            }
        }
        return isRegistered;
    }

    public List<vesting_balance_object> get_vesting_balances(String name) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        List<vesting_balance_object> vestingList = null;
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_vesting_balances");

        List<Object> listParams = new ArrayList<>();
        listParams.add(name);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>() {}.getType());
        String strResp;
        try {
            strResp = sendForJsonReply(callObject, replyObject);
        } catch (Exception e) {
            strResp = replyObject.getResponse();
        }
        if (strResp == null)
            strResp = replyObject.getResponse();
        if (strResp != null) {
            try {
                JSONObject jsonObject = new JSONObject(strResp);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                if (jsonArray.length() > 0) {
                    vestingList = new ArrayList<vesting_balance_object>();
                    for (int i = 0 ; i< jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj != null) {
                            String id = obj.getString("id");
                            String owner = obj.getString("owner");
                            long balance_amount = obj.getJSONObject("balance").getLong("amount");
                            if (balance_amount>0) {
                                String balance_asset_id = obj.getJSONObject("balance").getString("asset_id");
                                JSONArray policyArray = obj.getJSONArray("policy");
                                if (policyArray.length() > 0) {
                                    long vesting_seconds = policyArray.getJSONObject(1).getLong("vesting_seconds");
                                    long coin_seconds_earned = policyArray.getJSONObject(1).getLong("coin_seconds_earned");
                                    String strTotal = CalculateUtils.mul(balance_amount, vesting_seconds);
                                    double nTotal = Double.parseDouble(strTotal);

                                    String strPersent = CalculateUtils.div(coin_seconds_earned, nTotal, 12);
                                    double availablePercent = Double.parseDouble(strPersent);
                                    double allowedAmount = Double.parseDouble(CalculateUtils.mul(balance_amount, availablePercent));

                                    //coin_seconds_earned/(60*60*24)
                                    double coin_days_earned = Double.parseDouble(CalculateUtils.div(coin_seconds_earned, 86400, 12));

                                    //balance_amount*vesting_seconds/(60*60*24)
                                    double coin_days_required = Double.parseDouble(CalculateUtils.div(Double.parseDouble(strTotal), 86400, 12));

                                    //vesting_seconds*(1-availablePercent)/(60*60*24)
                                    double vesting_period = Double.parseDouble(CalculateUtils.div(Double.parseDouble(CalculateUtils.mul(vesting_seconds, CalculateUtils.sub(1,availablePercent))), 86400, 2));

                                    vesting_balance_object vesting = new vesting_balance_object();
                                    vesting.vesting_seconds = vesting_seconds+"";
                                    vesting.id = object_id.create_from_string(id);
                                    vesting.owner = owner;
                                    vesting.TotalBalance = CalculateUtils.div(balance_amount, 100000, 5);
                                    vesting.AvailablePersent = CalculateUtils.div(availablePercent,1,4);
                                    vesting.Available_to_claim = CalculateUtils.div(allowedAmount, 100000, 5);
                                    vesting.coin_seconds_earned = coin_seconds_earned;
                                    vesting.coin_days_earned = CalculateUtils.div(coin_days_earned, 100000, 0);
                                    vesting.coin_days_required = CalculateUtils.div(coin_days_required, 100000, 0);
                                    vesting.vesting_period = vesting_period+"";
                                    vestingList.add(vesting);
                                }
                            } else {
                                vesting_balance_object vesting = new vesting_balance_object();
                                vesting.id = object_id.create_from_string(id);
                                vesting.owner = owner;
                                vesting.TotalBalance = "0";
                                vesting.AvailablePersent = "0";
                                vesting.Available_to_claim = "0.00000";
                                vestingList.add(vesting);
                            }
                        }
                    }
                    return vestingList;
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return vestingList;
    }

    public AllHistory get_all_history(String baseSymbolId, String qouteSymbolId, int nLimit) throws NetworkStatusException {
        _nHistoryId = get_history_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nHistoryId);
        callObject.params.add("get_fill_order_history");

        List<Object> listAccountHistoryParam = new ArrayList<>();
        listAccountHistoryParam.add(baseSymbolId);
        listAccountHistoryParam.add(qouteSymbolId);
        listAccountHistoryParam.add(nLimit);
        callObject.params.add(listAccountHistoryParam);

        ReplyObjectProcess<Reply<AllHistory>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<AllHistory>>(){}.getType());
        Reply<AllHistory> replyObject = sendForReply(callObject, replyObjectProcess);

        Gson gson = new Gson();
        AllHistory allHistory = gson.fromJson(replyObjectProcess.getResponse(),AllHistory.class);
        return allHistory;
    }

    public MarketTicker get_ticker(String base, String quote) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_ticker");

        List<Object> listParams = new ArrayList<>();
        listParams.add(base);
        listParams.add(quote);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<MarketTicker>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<MarketTicker>>(){}.getType());
        Reply<MarketTicker> reply = sendForReply(callObject, replyObject);

        if (reply == null) {
            return null;
        } else {
            return reply.result;
        }
    }

    public OrderBook get_order_book(String base, String quote, int limit) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_order_book");

        List<Object> listParams = new ArrayList<>();
        listParams.add(base);
        listParams.add(quote);
        listParams.add(limit);
        callObject.params.add(listParams);
        ReplyObjectProcess<Reply<OrderBook>> replyObjectProcess =
                new ReplyObjectProcess<>(new TypeToken<Reply<OrderBook>>(){}.getType());
        Reply<OrderBook> replyObject = sendForReply(callObject, replyObjectProcess);

        if (replyObject == null) {
            return null;
        } else {
            return replyObject.result;
        }
    }

    public operations.operation_type get_history_object(Integer object) throws NetworkStatusException {
        _nDatabaseId = get_database_api_id();
        Call callObject = new Call();
        callObject.id = mnCallId.getAndIncrement();
        callObject.method = "call";
        callObject.params = new ArrayList<>();
        callObject.params.add(_nDatabaseId);
        callObject.params.add("get_objects");

        List<Object> listParams = new ArrayList<>();
        List list = new ArrayList();

        list.add("1.11." + object);

        listParams.add(list);
        callObject.params.add(listParams);

        ReplyObjectProcess<Reply<List<limit_order_object>>> replyObject =
                new ReplyObjectProcess<>(new TypeToken<Reply<List<limit_order_object>>>() {}.getType());
        String strResp;
        try {
            strResp = sendForJsonReply(callObject, replyObject);
        } catch (Exception e) {
            strResp = replyObject.getResponse();
        }

        if (strResp == null)
            strResp = replyObject.getResponse();
        if (strResp != null) {
            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(strResp);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                if (jsonArray.length() > 0) {
                    JSONObject obj = jsonArray.getJSONObject(0);

                    Gson gson = global_config_object.getInstance().getGsonBuilder().create();

                    operations.operation_type operation_type = gson.fromJson(obj.getJSONArray("op").toString(), operations.operation_type.class);

                    return operation_type;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
