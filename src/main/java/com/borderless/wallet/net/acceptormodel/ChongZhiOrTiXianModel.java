package com.borderless.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/17.
 */

public class ChongZhiOrTiXianModel extends ResponseModelBase {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String orderNo;
        private String memberBdsAccount;
        private String symbol;
        private String memberName;
        private String amount;
        private String orderStatusCode;
        private String orderStatus;
        private String createDate;

        public String getMemberBdsAccount() {
            return memberBdsAccount;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public void setMemberBdsAccount(String memberBdsAccount) {
            this.memberBdsAccount = memberBdsAccount;
        }


        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getOrderStatusCode() {
            return orderStatusCode;
        }

        public void setOrderStatusCode(String orderStatusCode) {
            this.orderStatusCode = orderStatusCode;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }


        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }


}
