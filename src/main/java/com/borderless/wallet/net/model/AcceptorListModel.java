package com.borderless.wallet.net.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/2.
 */

public class AcceptorListModel {

    private String status;
    private String msg;
    private ArrayList<Bean> data;

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

    public ArrayList<Bean> getData() {
        return data;
    }

    public void setData(ArrayList<Bean> data) {
        this.data = data;
    }

    public static class Bean{
         private String rownum;
         private String acceptantBdsAccount;
         private String acceptantName;
         private String dealCount;
         private String dealAmount;

        private String cashInUpperLimit; // 充值上限
        private String cashOutUpperLimit; // 提现上限
        private String cashInLowerLimit; // 充值下限
        private String cashOutLowerLimit;// 提现下限
        private String symbol;
        private String bdsAccountCo;
        private String cashInUncompletedAmount;
        private String cashInLock;

        public String getBdsAccountCo() {
            return bdsAccountCo;
        }

        public void setBdsAccountCo(String bdsAccountCo) {
            this.bdsAccountCo = bdsAccountCo;
        }

        public String getCashInUncompletedAmount() {
            return cashInUncompletedAmount;
        }

        public void setCashInUncompletedAmount(String cashInUncompletedAmount) {
            this.cashInUncompletedAmount = cashInUncompletedAmount;
        }

        public String getCashInLock() {
            return cashInLock;
        }

        public void setCashInLock(String cashInLock) {
            this.cashInLock = cashInLock;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Bean() {
        }

        public Bean(String rownum, String bdsAccount, String name, String dealCount, String dealAmount) {
            this.rownum = rownum;
            this.acceptantBdsAccount = bdsAccount;
            this.acceptantName = name;
            this.dealCount = dealCount;
            this.dealAmount = dealAmount;
        }

        public String getCashInUpperLimit() {
            return cashInUpperLimit;
        }

        public void setCashInUpperLimit(String cashInUpperLimit) {
            this.cashInUpperLimit = cashInUpperLimit;
        }

        public String getCashOutUpperLimit() {
            return cashOutUpperLimit;
        }

        public void setCashOutUpperLimit(String cashOutUpperLimit) {
            this.cashOutUpperLimit = cashOutUpperLimit;
        }

        public String getCashInLowerLimit() {
            return cashInLowerLimit;
        }

        public void setCashInLowerLimit(String cashInLowerLimit) {
            this.cashInLowerLimit = cashInLowerLimit;
        }

        public String getCashOutLowerLimit() {
            return cashOutLowerLimit;
        }

        public void setCashOutLowerLimit(String cashOutLowerLimit) {
            this.cashOutLowerLimit = cashOutLowerLimit;
        }

        public String getRownum() {
            return rownum;
        }

        public void setRownum(String rownum) {
            this.rownum = rownum;
        }

        public String getAcceptantBdsAccount() {
            return acceptantBdsAccount;
        }

        public void setAcceptantBdsAccount(String acceptantBdsAccount) {
            this.acceptantBdsAccount = acceptantBdsAccount;
        }

        public String getAcceptantName() {
            return acceptantName;
        }

        public void setAcceptantName(String acceptantName) {
            this.acceptantName = acceptantName;
        }

        public String getDealCount() {
            return dealCount;
        }

        public void setDealCount(String dealCount) {
            this.dealCount = dealCount;
        }

        public String getDealAmount() {
            return dealAmount;
        }

        public void setDealAmount(String dealAmount) {
            this.dealAmount = dealAmount;
        }
    }
}

