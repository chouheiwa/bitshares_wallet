package com.borderless.wallet.net.model;

import java.util.ArrayList;

/**
 * Created by daihongwei on 2018/1/3.
 */

public class HttpResponseModel {

    /**
     * status : success
     * msg :
     * data :
     */

    private String status;
    private String msg;
    private ArrayList<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

   public class DataBean{
        String value;
        CurrentFeed current_feed;

       public CurrentFeed getCurrent_feed() {
           return current_feed;
       }

       public void setCurrent_feed(CurrentFeed current_feed) {
           this.current_feed = current_feed;
       }

       public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public class CurrentFeed{
        CoreExchangeRate core_exchange_rate;
        SettlementPrice settlement_price;
        String maintenance_collateral_ratio;

        public String getMaintenance_collateral_ratio() {
            return maintenance_collateral_ratio;
        }

        public void setMaintenance_collateral_ratio(String maintenance_collateral_ratio) {
            this.maintenance_collateral_ratio = maintenance_collateral_ratio;
        }

        public CoreExchangeRate getCore_exchange_rate() {
            return core_exchange_rate;
        }

        public void setCore_exchange_rate(CoreExchangeRate core_exchange_rate) {
            this.core_exchange_rate = core_exchange_rate;
        }

        public SettlementPrice getSettlement_price() {
            return settlement_price;
        }

        public void setSettlement_price(SettlementPrice settlement_price) {
            this.settlement_price = settlement_price;
        }
    }

    public class SettlementPrice{
        Asset base;
        Asset quote;

        public Asset getBase() {
            return base;
        }

        public void setBase(Asset base) {
            this.base = base;
        }

        public Asset getQuote() {
            return quote;
        }

        public void setQuote(Asset quote) {
            this.quote = quote;
        }
    }


    public class CoreExchangeRate{
        Asset base;
        Asset quote;

        public Asset getBase() {
            return base;
        }

        public void setBase(Asset base) {
            this.base = base;
        }

        public Asset getQuote() {
            return quote;
        }

        public void setQuote(Asset quote) {
            this.quote = quote;
        }
    }

    public class Asset{
        String amount;
        String asset_id;

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
        }

        public String getAmount() {

            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
