package com.borderless.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AcceptorResponseModel extends ResponseModelBase {
    private List<DataBean> data;

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
