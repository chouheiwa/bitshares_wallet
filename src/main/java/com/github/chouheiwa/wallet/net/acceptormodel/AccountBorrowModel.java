package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class AccountBorrowModel extends ResponseModelBase {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String minBorrow;
        private String maxBorrow;

        public String getMaxBorrow() {
            return maxBorrow;
        }

        public void setMaxBorrow(String maxBorrow) {
            this.maxBorrow = maxBorrow;
        }

        public String getMinBorrow() {
            return minBorrow;
        }

        public void setMinBorrow(String minBorrow) {
            this.minBorrow = minBorrow;
        }
    }
}
