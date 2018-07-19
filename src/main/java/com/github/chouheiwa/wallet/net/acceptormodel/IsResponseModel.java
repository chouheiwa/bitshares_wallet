package com.github.chouheiwa.wallet.net.acceptormodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class IsResponseModel {


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

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean  implements Serializable{

        private String isVerified;
        private String isAcceptant;
        private String mortgageLowerLimit;

        public String getMortgageLowerLimit() {
            return mortgageLowerLimit;
        }

        public void setMortgageLowerLimit(String mortgageLowerLimit) {
            this.mortgageLowerLimit = mortgageLowerLimit;
        }

        public String getIsVerified() {
            return isVerified;
        }

        public void setIsVerified(String isVerified) {
            this.isVerified = isVerified;
        }

        public String getIsAcceptant() {
            return isAcceptant;
        }

        public void setIsAcceptant(String isAcceptant) {
            this.isAcceptant = isAcceptant;
        }
    }

}
