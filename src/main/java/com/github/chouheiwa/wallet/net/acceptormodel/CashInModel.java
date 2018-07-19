package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public class CashInModel extends ResponseModelBase {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String address;
        private String bdsAccount;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBdsAccount() {
            return bdsAccount;
        }

        public void setBdsAccount(String bdsAccount) {
            this.bdsAccount = bdsAccount;
        }
    }

}
