package com.borderless.wallet.net.model;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class MarketRecordResponseModel {
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
        //Count":"1.0000","Price":"0.25000","Turnover":"0.250000000","LegalMoney":"USD","CreateDate":"2017/11/3 16:11:28","Type":"0"},{"
    public static class DataBean {

        private String id;
        private String Status;
        private String LegalMoney;
        private String Price;
        private String DealCount;
        private String Count;
        private String Turnover;
        private String Type;
        private String CreateDate;
       private String price;



        public String getprice() {
            return price;
        }

        public void setprice(String price) {
                this.price = price;
            }




        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getDealCount() {
            return DealCount;
        }

        public void setDealCount(String dealCount) {
            DealCount = dealCount;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String createDate) {
            CreateDate = createDate;
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
            return Price;
        }

        public void setPrice(String price) {
            this.Price = price;
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
