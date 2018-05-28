package com.borderless.wallet.socket.chain;

import com.borderless.wallet.socket.asset;
import com.borderless.wallet.socket.fc.io.base_encoder;
import com.borderless.wallet.socket.fc.io.raw_type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class price_feed {

    public price settlement_price;
    public short maintenance_collateral_ratio = 1000;
    public short maximum_short_squeeze_ratio = 1000;
    public price core_exchange_rate;


    public void write_to_encoder(base_encoder baseEncoder) {
        settlement_price.write_to_encoder(baseEncoder);
        raw_type rawObject = new raw_type();
        baseEncoder.write(rawObject.get_byte_array(maintenance_collateral_ratio));
        baseEncoder.write(rawObject.get_byte_array(maximum_short_squeeze_ratio));
        core_exchange_rate.write_to_encoder(baseEncoder);
    }
}
