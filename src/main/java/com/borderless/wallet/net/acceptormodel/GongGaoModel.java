package com.borderless.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */

public class GongGaoModel extends ResponseModelBase{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String vaule;
        private String Column1;
        private String status;


        public String getColumn1() {
            return Column1;
        }

        public void setColumn1(String column1) {
            Column1 = column1;
        }

        public String getVaule() {
            return vaule;
        }

        public void setVaule(String vaule) {
            this.vaule = vaule;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
