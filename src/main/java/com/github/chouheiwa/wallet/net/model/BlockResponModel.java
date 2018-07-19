package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class BlockResponModel {

    /**
     * status : success
     * msg :
     * data : [{"blocknumber":"#190823","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190822","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190821","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190820","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190819","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190818","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190817","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190816","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190815","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"},{"blocknumber":"#190814","time":"2017/9/8 3:33:00","witness":"1.6.6","exchangecount":"0"}]
     */

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

    public static class DataBean {
        /**
         * blocknumber : #190823
         * time : 2017/9/8 3:33:00
         * witness : 1.6.6
         * exchangecount : 0
         */

        private String blocknumber;
        private String time;
        private String witness;
        private String exchangecount;

        public String getBlocknumber() {
            return blocknumber;
        }

        public void setBlocknumber(String blocknumber) {
            this.blocknumber = blocknumber;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWitness() {
            return witness;
        }

        public void setWitness(String witness) {
            this.witness = witness;
        }

        public String getExchangecount() {
            return exchangecount;
        }

        public void setExchangecount(String exchangecount) {
            this.exchangecount = exchangecount;
        }
    }
}
