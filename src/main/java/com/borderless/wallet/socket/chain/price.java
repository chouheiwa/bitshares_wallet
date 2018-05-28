package com.borderless.wallet.socket.chain;

import com.borderless.wallet.socket.asset;
import com.borderless.wallet.socket.fc.io.base_encoder;
import com.borderless.wallet.socket.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;

public class price {
    public asset base;
    public asset quote;

    public price(asset assetBase, asset assetQuote) {
        base = assetBase;
        quote = assetQuote;
    }

    public static price unit_price(object_id<asset_object> assetObjectobjectId) {
        return new price(new asset(1, assetObjectobjectId), new asset(1, assetObjectobjectId));
    }

    public void write_to_encoder(base_encoder baseEncoder) {
        base.write_to_encoder(baseEncoder);
        quote.write_to_encoder(baseEncoder);
    }
}
