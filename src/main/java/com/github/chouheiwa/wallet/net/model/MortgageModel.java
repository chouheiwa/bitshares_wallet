package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class MortgageModel {

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

        private String pay_cn;
        private String pay_en;



    }
}
