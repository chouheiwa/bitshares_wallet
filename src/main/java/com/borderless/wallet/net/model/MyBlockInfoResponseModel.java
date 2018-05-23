package com.borderless.wallet.net.model;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 */

public class MyBlockInfoResponseModel {
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

    public void setData(List<DataBean>  dataBean) {
        this.data = dataBean;
    }

    public static class DataBean {

        private String name;
        private String info;
        private String time;

        public DataBean(String name, String info, String time) {
            this.name = name;
            this.info = info;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
