package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class MarketListResponseModel {
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

        private String id;
        private String LegalMoney;
        private String price;
        private String Count;
        private String Turnover;
        private String Type;

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLegalMoney() {
            return LegalMoney;
        }

        public void setLegalMoney(String legalMoney) {
            LegalMoney = legalMoney;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String count) {
            Count = count;
        }

        public String getTurnover() {
            return Turnover;
        }

        public void setTurnover(String turnover) {
            Turnover = turnover;
        }
    }
}
