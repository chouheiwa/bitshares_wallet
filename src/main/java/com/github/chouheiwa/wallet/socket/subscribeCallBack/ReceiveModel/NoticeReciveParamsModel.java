package com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveModel;

import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class NoticeReciveParamsModel {
    public Integer id;

    public JsonArray detailParams;

    public static class NoticeReciveParamsModelDeserializer implements JsonDeserializer<NoticeReciveParamsModel> {

        @Override
        public NoticeReciveParamsModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            NoticeReciveParamsModel model = new NoticeReciveParamsModel();

            model.id = jsonArray.get(0).getAsInt();

            model.detailParams = jsonArray.get(1).getAsJsonArray();

            return model;
        }
    }
}
