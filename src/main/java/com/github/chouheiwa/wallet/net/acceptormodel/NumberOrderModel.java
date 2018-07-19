package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/29.
 *
 *   "": "DO20171229144108557520",
 "": "zxcvbn3",
 "": "BTC",
 "": "12.00000000",
 "": "",
 "": "NA",
 "CreateDate": "2017/12/29 14:41:08"
 */

public class NumberOrderModel extends ResponseModelBase {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String OrderNo;
        private String BdsAccount;
        private String Symbol;
        private String Amount;
        private String Fee;
        private String Status;
        private String CreateDate;
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String orderNo) {
            OrderNo = orderNo;
        }

        public String getBdsAccount() {
            return BdsAccount;
        }

        public void setBdsAccount(String bdsAccount) {
            BdsAccount = bdsAccount;
        }

        public String getSymbol() {
            return Symbol;
        }

        public void setSymbol(String symbol) {
            Symbol = symbol;
        }

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }

        public String getFee() {
            return Fee;
        }

        public void setFee(String fee) {
            Fee = fee;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String createDate) {
            CreateDate = createDate;
        }
    }
}

