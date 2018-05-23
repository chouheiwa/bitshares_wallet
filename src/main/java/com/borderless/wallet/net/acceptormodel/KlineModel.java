package com.borderless.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */

public class KlineModel extends ResponseModelBase{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String status;
        private String value;
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
