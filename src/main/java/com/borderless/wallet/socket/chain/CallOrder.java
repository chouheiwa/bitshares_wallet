package com.borderless.wallet.socket.chain;

import com.borderless.wallet.utils.TextUtils;

/**
 * Created by Administrator on 2017/11/24.
 */

public class CallOrder {
public String _id;
public String collateral;
public String debt;
public String base_asset_id;
public String quote_asset_id;
public String base_amount;
public String quote_amount;


    public String getQuote_amount() {
        if (TextUtils.isEmpty(quote_amount)){
            quote_amount = "0";
        }
        return quote_amount;
    }

    public void setQuote_amount(String quote_amount) {
        this.quote_amount = quote_amount;
    }

    public String getBase_amount() {
        if (TextUtils.isEmpty(base_amount)){
            base_amount = "0";
        }
        return base_amount;
    }

    public void setBase_amount(String base_amount) {
        this.base_amount = base_amount;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCollateral() {
        return collateral;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getBase_asset_id() {
        return base_asset_id;
    }

    public void setBase_asset_id(String base_asset_id) {
        this.base_asset_id = base_asset_id;
    }

    public String getQuote_asset_id() {
        return quote_asset_id;
    }

    public void setQuote_asset_id(String quote_asset_id) {
        this.quote_asset_id = quote_asset_id;
    }
}
