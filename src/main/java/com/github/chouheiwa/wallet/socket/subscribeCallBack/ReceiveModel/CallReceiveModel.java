package com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveModel;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CallReceiveModel {

    /**
     * 因区块链websocket返回值有两种
     * 1.为call方法后的标准返回值
     * 2.为notice通知返回
     * 该对象为call对象对应的返回值
     * 当收到的消息类型为notice该对象为空
     */
    public CallBackRecvModel callBackRecvModel;

    public NoticeRecvModel noticeRecvModel;

    public Integer getId() {
        if (callBackRecvModel != null) return callBackRecvModel.id;

        return noticeRecvModel.params.id;
    }

    public static class CallReceiveModelDeserializer implements JsonDeserializer<CallReceiveModel> {

        @Override
        public CallReceiveModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            CallReceiveModel callReceiveModel = new CallReceiveModel();

            if (jsonElement.getAsJsonObject().get("method") == null) {
                callReceiveModel.callBackRecvModel = jsonDeserializationContext.deserialize(jsonElement,CallBackRecvModel.class);
            }else {
                try {
                    callReceiveModel.noticeRecvModel = jsonDeserializationContext.deserialize(jsonElement,NoticeRecvModel.class);
                }catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return callReceiveModel;
        }
    }
}
