package com.borderless.wallet.net.acceptormodel;

/**
 * Created by dagou on 2017/9/21.
 */

public class ResponseModelBase {

    /**
     * status : success
     * msg :
     * data :
     */

    private String status;
    private String msg;


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

}
