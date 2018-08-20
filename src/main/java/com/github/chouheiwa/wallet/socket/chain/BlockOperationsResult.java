package com.github.chouheiwa.wallet.socket.chain;

import com.github.chouheiwa.wallet.socket.asset;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BlockOperationsResult {
    public Integer operationType;

    public Object operationContent;

    public static class BlockOperationsResultDeserializer implements JsonDeserializer<BlockOperationsResult> {

        @Override
        public BlockOperationsResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            BlockOperationsResult blockOperationsResult = new BlockOperationsResult();

            blockOperationsResult.operationType = jsonElement.getAsJsonArray().get(0).getAsInt();

            try {
                switch (blockOperationsResult.operationType) {
                    case 1:
                        blockOperationsResult.operationContent = object_id.create_from_string(jsonElement.getAsJsonArray().get(1).getAsString());
                        break;
                    case 2:
                        blockOperationsResult.operationContent = jsonDeserializationContext.deserialize(jsonElement.getAsJsonArray().get(1),asset.class);
                        break;
                    default:
                        blockOperationsResult.operationContent = new Object();
                        break;
                }
            }catch (Exception e) {
                blockOperationsResult.operationContent = null;
            }

            return blockOperationsResult;
        }
    }
}
