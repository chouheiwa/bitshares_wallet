package com.github.chouheiwa.wallet.socket.chain;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BlockOperationsResult {
    public Integer operationType;

    public object_id operationId;

    public static class BlockOperationsResultDeserializer implements JsonDeserializer<BlockOperationsResult> {

        @Override
        public BlockOperationsResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            BlockOperationsResult blockOperationsResult = new BlockOperationsResult();

            blockOperationsResult.operationType = jsonElement.getAsJsonArray().get(0).getAsInt();

            blockOperationsResult.operationId = object_id.create_from_string(jsonElement.getAsJsonArray().get(1).getAsString());

            return blockOperationsResult;
        }
    }
}
