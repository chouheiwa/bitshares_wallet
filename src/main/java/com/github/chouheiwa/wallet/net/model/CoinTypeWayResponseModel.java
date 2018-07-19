package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class CoinTypeWayResponseModel {


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

        private String Bank; //开户行
        private String Name;//姓名
        private String Account;//卡号
        private String cointypeway;//支付方式
        private String pic; //微信、支付宝图片
        private String free;
        private String cointypewayid;

        public String getCointypewayid() {
            return cointypewayid;
        }

        public void setCointypewayid(String cointypewayid) {
            this.cointypewayid = cointypewayid;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getBank() {
            return Bank;
        }

        public void setBank(String bank) {
            Bank = bank;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getAccount() {
            return Account;
        }

        public void setAccount(String account) {
            Account = account;
        }

        public String getCointypeway() {
            return cointypeway;
        }

        public void setCointypeway(String cointypeway) {
            this.cointypeway = cointypeway;
        }
    }
}
