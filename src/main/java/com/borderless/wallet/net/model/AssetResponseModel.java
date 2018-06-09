package com.borderless.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class AssetResponseModel {

    /**
     * status : success
     * msg :
     * data : [{"amount":39522118,"asset_id":"1.3.0"}]
     */

    private String status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : 39522118
         * asset_id : 1.3.0
         */

        private long amount;
        private String asset_id;

        public long getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
        }
    }
}
