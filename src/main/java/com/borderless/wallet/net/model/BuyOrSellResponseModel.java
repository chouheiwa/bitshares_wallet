package com.borderless.wallet.net.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/9.
 */

public class BuyOrSellResponseModel {
    private String status;
    private String msg;
    private ArrayList<DataBean> dataBean;

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

    public ArrayList<DataBean> getDataBean() {
        return dataBean;
    }

    public void setDataBean(ArrayList<DataBean>  dataBean) {
        this.dataBean = dataBean;
    }

    public static class DataBean {
        private String type;//类型
        private String amount;//数量
        private String price;//最新价格
        private String gmv; //

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGmv() {
            return gmv;
        }

        public void setGmv(String cmv) {
            this.gmv = cmv;
        }
    }

}
