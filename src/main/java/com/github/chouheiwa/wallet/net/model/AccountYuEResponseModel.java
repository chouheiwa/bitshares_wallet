package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/21.
 */

public class AccountYuEResponseModel {

    /**
     * status : success
     * msg :
     * data :
     */

    private String status;
    private String msg;
    private List<NewObject> data;

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

    public List<NewObject> getData() {
        return data;
    }

    public void setData(List<NewObject> data) {
        this.data = data;
    }


    public class NewObject {

        String amount;
        String asset_id;

        public String getAmount() {
            return amount;
        }

        public String getAsset_id() {
            return asset_id;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
        }
    }
}
