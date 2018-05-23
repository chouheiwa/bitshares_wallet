package com.borderless.wallet.net.model;

import java.util.ArrayList;

/**
 * Created by dagou on 2017/9/21.
 */

public class IsAccResponseModel {

    /**
     * status : success
     * msg :
     * data :
     */

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

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public class DataBean {

        String count;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

}
