package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class CoinTypeResponseModel {


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

        private String cointyp; //类型
        private String PayTypeID;//id

        public String getCointyp() {
            return cointyp;
        }

        public void setCointyp(String cointyp) {
            this.cointyp = cointyp;
        }

        public String getPayTypeID() {
            return PayTypeID;
        }

        public void setPayTypeID(String payTypeID) {
            PayTypeID = payTypeID;
        }
    }
}
