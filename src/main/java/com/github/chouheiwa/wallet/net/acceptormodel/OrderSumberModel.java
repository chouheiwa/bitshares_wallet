package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/17.
 */

public class OrderSumberModel extends ResponseModelBase {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String cashInUncompletedCount;
        private String cashOutUncompletedCount;
        private String 	cashInUncompletedAmount;
        private String cashOutUncompletedAmount;

        public String getCashInUncompletedCount() {
            return cashInUncompletedCount;
        }

        public void setCashInUncompletedCount(String cashInUncompletedCount) {
            this.cashInUncompletedCount = cashInUncompletedCount;
        }

        public String getCashOutUncompletedCount() {
            return cashOutUncompletedCount;
        }

        public void setCashOutUncompletedCount(String cashOutUncompletedCount) {
            this.cashOutUncompletedCount = cashOutUncompletedCount;
        }

        public String getCashInUncompletedAmount() {
            return cashInUncompletedAmount;
        }

        public void setCashInUncompletedAmount(String cashInUncompletedAmount) {
            this.cashInUncompletedAmount = cashInUncompletedAmount;
        }

        public String getCashOutUncompletedAmount() {
            return cashOutUncompletedAmount;
        }

        public void setCashOutUncompletedAmount(String cashOutUncompletedAmount) {
            this.cashOutUncompletedAmount = cashOutUncompletedAmount;
        }
    }
}
