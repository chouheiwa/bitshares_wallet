package com.borderless.wallet.net.model;

/**
 * Created by dagou on 2017/9/21.
 */

public class RegisterResponseModel {

    /**
     * status : success
     * msg :
     * data :
     */

    private String status;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
