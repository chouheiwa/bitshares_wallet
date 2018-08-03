package com.github.chouheiwa.wallet.socket.chain;

import com.github.chouheiwa.wallet.utils.TimeUtils;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class block_object {
        public String timeStame;
        public String blockNumber;
        public String witnessId;
        public int transactionCount;
        public ArrayList<String> transaction_ids;
        public List<transaction> transactions;


        public static class block_object_deserializer implements JsonDeserializer<block_object> {

                @Override
                public block_object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        block_object block = new block_object();
                        JsonObject dataobj = jsonElement.getAsJsonObject();
                        block.timeStame = TimeUtils.utc2Local(dataobj.get("timestamp").getAsString().replace("T"," "));

                        block.witnessId = dataobj.get("witness").getAsString();
                        JsonArray jarray = dataobj.getAsJsonArray("transactions");
                        block.transactionCount = jarray.size();
                        block.transaction_ids = new ArrayList<>();

                        block.transactions = new ArrayList<>();

                        for (int i = 0; i < jarray.size();i ++) {
                                transaction trans = jsonDeserializationContext.deserialize(jarray.get(i),transaction.class);

                                block.transaction_ids.add(trans.ids().toString().substring(0,40));

                                block.transactions.add(trans);
                        }

                        return block;
                }
        }
}
