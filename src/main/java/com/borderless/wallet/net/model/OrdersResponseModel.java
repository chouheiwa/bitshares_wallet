package com.borderless.wallet.net.model;

import de.bitsharesmunich.graphenej.LimitOrder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/9.
 */

public class OrdersResponseModel {
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

        private String price;//最新价格
        private String btc;
        private String bds;
        private String time;
        private LimitOrder limitOrder;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LimitOrder getLimitOrder() {
            return limitOrder;
        }

        public void setLimitOrder(LimitOrder limitOrder) {
            this.limitOrder = limitOrder;
        }

        public DataBean(String price, String btc, String bds, String time, LimitOrder limitOrder, String type) {
            this.price = price;
            this.btc = btc;
            this.bds = bds;
            this.time = time;
            this.limitOrder = limitOrder;
            this.type = type;
        }

        public DataBean() {
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBtc() {
            return btc;
        }

        public void setBtc(String btc) {
            this.btc = btc;
        }

        public String getBds() {
            return bds;
        }

        public void setBds(String bds) {
            this.bds = bds;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
