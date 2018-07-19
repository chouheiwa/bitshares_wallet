package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */

public class FeeMinOrMaxModel extends ResponseModelBase {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String cashinmin;
        private String cashinmax;
        private String cashoutmin;
        private String cashoutmax;

        public String getCashinmin() {
            return cashinmin;
        }

        public void setCashinmin(String cashinmin) {
            this.cashinmin = cashinmin;
        }

        public String getCashinmax() {
            return cashinmax;
        }

        public void setCashinmax(String cashinmax) {
            this.cashinmax = cashinmax;
        }

        public String getCashoutmin() {
            return cashoutmin;
        }

        public void setCashoutmin(String cashoutmin) {
            this.cashoutmin = cashoutmin;
        }

        public String getCashoutmax() {
            return cashoutmax;
        }

        public void setCashoutmax(String cashoutmax) {
            this.cashoutmax = cashoutmax;
        }
    }
}
