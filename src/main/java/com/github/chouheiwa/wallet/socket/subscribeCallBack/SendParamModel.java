package com.github.chouheiwa.wallet.socket.subscribeCallBack;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class SendParamModel {

    public static class SendParamModelSerializer implements JsonSerializer<SendParamModel>{
        @Override
        public JsonElement serialize(SendParamModel sendParamModel, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonArray jsonArray = new JsonArray();

            jsonArray.add(sendParamModel.connectDataBaseId);

            jsonArray.add(sendParamModel.callMethod);

            jsonArray.add(jsonSerializationContext.serialize(sendParamModel.params));

            return jsonArray;
        }
    }


    public Integer connectDataBaseId;

    public String callMethod;

    public List<Object> params;

    public SendParamModel(Integer connectDataBaseId, String callMethod, List<Object> params) {
        this.connectDataBaseId = connectDataBaseId;
        this.callMethod = callMethod;
        this.params = params;
    }
}
