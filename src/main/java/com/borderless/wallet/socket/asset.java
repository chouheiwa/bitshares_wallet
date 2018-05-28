package com.borderless.wallet.socket;

import com.borderless.wallet.socket.chain.asset_object;
import com.borderless.wallet.socket.chain.object_id;
import com.borderless.wallet.socket.chain.price;
import com.borderless.wallet.socket.fc.io.base_encoder;
import com.borderless.wallet.socket.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;

import java.math.BigInteger;

public class asset {
    public long amount;
    public object_id<asset_object> asset_id;

    public asset(long lAmount, object_id<asset_object> assetObjectobjectId) {
        amount = lAmount;
        asset_id = assetObjectobjectId;
    }

    public asset multipy(price priceObject) {
        BigInteger bigAmount = BigInteger.valueOf(amount);
        BigInteger bigQuoteAmount = BigInteger.valueOf(priceObject.quote.amount);
        BigInteger bigBaseAmount = BigInteger.valueOf(priceObject.base.amount);
        if (asset_id.equals(priceObject.base.asset_id)) {
            BigInteger bigResult = bigAmount.multiply(bigQuoteAmount).divide(bigBaseAmount);
            return new asset(bigResult.longValue(), priceObject.quote.asset_id);

        } else if (asset_id.equals(priceObject.quote.asset_id)) {
            BigInteger bigResult = bigAmount.multiply(bigBaseAmount).divide(bigQuoteAmount  );
            return new asset(bigResult.longValue(), priceObject.base.asset_id);
        } else {
            throw new RuntimeException("invalid price object");
        }
    }

    public void write_to_encoder(base_encoder baseEncoder) {
        raw_type rawObject = new raw_type();

        baseEncoder.write(rawObject.get_byte_array(this.amount));

        rawObject.pack(baseEncoder,UnsignedInteger.fromIntBits(this.asset_id.get_instance()));
    }
}
