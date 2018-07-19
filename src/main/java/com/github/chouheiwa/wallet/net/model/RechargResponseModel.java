package com.github.chouheiwa.wallet.net.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/9.
 */

public class RechargResponseModel {
    private String status;
    private String msg;


    private ArrayList<DataBean> data;

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

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean>  dataBean) {
        this.data = dataBean;
    }


    public static class DataBean {

        private String token;
        private String id;

        public String getId() {return id;}

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


    }
}
