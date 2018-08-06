package com.github.chouheiwa.wallet.socket.subscribeCallBack;

import com.github.chouheiwa.wallet.socket.chain.block_object;
import com.github.chouheiwa.wallet.socket.chain.global_config_object;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveModel.BlockInfo.CurrentBlock;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveModel.CallReceiveModel;
import com.github.chouheiwa.wallet.socket.websocketClient.websocketClient;
import com.github.chouheiwa.wallet.socket.websocketClient.websocketInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class WebsocketCallBackApi implements websocketInterface  {

    private Integer dataBaseId = -1;

    private List<CallBackUploadAbstractModel> needDoList = new ArrayList<>();

    private ConcurrentHashMap<Integer,CallBackUploadAbstractModel> hashMap = new ConcurrentHashMap<>();

    private Integer totalId = 0;

    private boolean opening = false;

    private websocketClient mWebsocket;

    public Integer getTotalId() {
        totalId ++;
        return totalId;
    }

    public boolean connect(String server) {
        try {
            mWebsocket = new websocketClient(server,this);
            mWebsocket.connect();
//            System.out.println("开始连接" + strServer);
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

    public void close() {
        mWebsocket.close();
    }

    /**
     * 当错误发生的时候(一般是区块链连接异常)
     * @param e 异常
     */
    public abstract void onErrorException(Exception e);

    @Override
    public void onOpen() {
        opening = true;

        websocketOpenDoneThing();
    }

    @Override
    public void onMessage(String resultMsg) {
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();

        CallReceiveModel callReceiveModel = gson.fromJson(resultMsg,CallReceiveModel.class);

        CallBackUploadAbstractModel model = hashMap.get(callReceiveModel.getId());

        System.out.println(resultMsg);

        if (model != null) {
            if (callReceiveModel.callBackRecvModel != null) model.reciveCallMessage(callReceiveModel.callBackRecvModel.result);
            if (callReceiveModel.noticeRecvModel != null) model.reciveNoticeMessage(callReceiveModel.noticeRecvModel.params.detailParams);

            if (model.isOnlyCall()) hashMap.remove(callReceiveModel.getId());
        }
    }

    @Override
    public void onError(Exception e) {
        this.onErrorException(e);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        this.close();
        this.onErrorException(new IOException());
    }

    /**
     * websocket连接开启后调用的方法(获取一系列id与订阅相关操作)
     */
    private void websocketOpenDoneThing() {
        //初始化获取database
        CallBackUploadAbstractModel model = new CallBackUploadAbstractModel("database",new ArrayList<>()) {
            @Override
            public void reciveCallMessage(JsonElement result) {
                System.out.println(result);

                dataBaseId = result.getAsInt();

                for (CallBackUploadAbstractModel needDo : needDoList) {
                    sendMessage(needDo);
                }

                needDoList.clear();
            }
            @Override
            public void reciveNoticeMessage(JsonElement result) {

            }
        };

        sendMessage(model);
    }

    public void registerReceive(ReceiveActionInterface actionInterface) {
        List<Object> subscribeCallBack = new ArrayList<>();

        subscribeCallBack.add(false);

        Gson gson = global_config_object.getInstance().getGsonBuilder().create();

        this.sendMessage(new CallBackUploadAbstractModel(false,CallBackUploadAbstractModel.UploadKind.uploadKindDataBase,"set_subscribe_callback",subscribeCallBack) {
            //这个方法会返回一次null
            @Override
            public void reciveCallMessage(JsonElement result) {

            }
            //这个方法将在订阅发起后返回
            @Override
            public void reciveNoticeMessage(JsonElement result) {
                try {
                    CurrentBlock currentBlock = gson.fromJson(result.getAsJsonArray().get(0).getAsJsonArray().get(0),CurrentBlock.class);

                    receiveCurrentBlock(currentBlock,actionInterface);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        List<Object> getObjectsList = new ArrayList<>();

        String[] objects = {"2.1.0"};

        getObjectsList.add(objects);

        this.sendMessage(new CallBackUploadAbstractModel(true,CallBackUploadAbstractModel.UploadKind.uploadKindDataBase,"get_objects",getObjectsList) {
            @Override
            public void reciveCallMessage(JsonElement result) {
                try {
                    JsonArray jsonArray = result.getAsJsonArray();

                    CurrentBlock currentBlock = gson.fromJson(jsonArray.get(0),CurrentBlock.class);

                    receiveCurrentBlock(currentBlock,actionInterface);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void reciveNoticeMessage(JsonElement result) {
            }
        });
    }

    private void receiveCurrentBlock(CurrentBlock currentBlock, ReceiveActionInterface actionInterface) {
        List<Object> list = new ArrayList<>();

        list.add(currentBlock.head_block_number);

        sendMessage(new CallBackUploadAbstractModel(true,CallBackUploadAbstractModel.UploadKind.uploadKindDataBase,"get_block",list) {
            @Override
            public void reciveCallMessage(JsonElement result) {
                Gson gson = global_config_object.getInstance().getGsonBuilder().create();
                block_object block_object = gson.fromJson(result, com.github.chouheiwa.wallet.socket.chain.block_object.class);

                block_object.blockNumber = currentBlock.head_block_number + "";

                actionInterface.onReceiveBlockAction(block_object);
            }

            @Override
            public void reciveNoticeMessage(JsonElement result) {

            }
        });

    }

    private Integer getIdWithKind(CallBackUploadAbstractModel.UploadKind uploadKind) {
        switch (uploadKind) {
            case uploadKindBase:
                return 1;

            case uploadKindDataBase:
                return dataBaseId;
        }

        return -1;
    }

    public void sendMessage(CallBackUploadAbstractModel abstractModel) {
        //判断当前websocket连接状态,如果没有开启则不允许进入
        //下一步是业务相关(需要先获取dataBaseId才能进行订阅等相关操作),因此当没有获得操作业务号编号时,将需要进行的操作放入待执行区,等待操作完成后依次将数据发送
        if (!opening || (dataBaseId == -1 && abstractModel.getUploadKind() != CallBackUploadAbstractModel.UploadKind.uploadKindBase)) {
            needDoList.add(abstractModel);
            return;
        }

        abstractModel.getCallBackUploadModel().id = this.getTotalId();

        Gson gson = global_config_object.getInstance().getGsonBuilder().create();

        hashMap.put(abstractModel.getCallBackUploadModel().id,abstractModel);

        List<Object> list = abstractModel.getCallParams();
        //如果是订阅,就将id插入到队首
        if (!abstractModel.isOnlyCall()) list.add(0,abstractModel.getCallBackUploadModel().id);

        abstractModel.getCallBackUploadModel().params = new SendParamModel(getIdWithKind(abstractModel.getUploadKind()),abstractModel.getCallMethod(),abstractModel.getCallParams());

        mWebsocket.send(gson.toJson(abstractModel.getCallBackUploadModel()));
    }

}
