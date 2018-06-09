package com.borderless.wallet.net.model;

import java.util.List;

/**
 * Created by 18301 on 2018/1/7.
 */

public class CoinList {

    /**
     * status : success
     * msg :
     * data : [{"coins":"CNY","deputycoin":"BDS","de":"1"},{"coins":"USD","deputycoin":"BDS","de":"1"},{"coins":"CNY","deputycoin":"BTC","de":"0"},{"coins":"CNY","deputycoin":"LTC","de":"0"},{"coins":"CNY","deputycoin":"ETH","de":"0"},{"coins":"CNY","deputycoin":"ALD","de":"0"},{"coins":"CNY","deputycoin":"TIVALUE","de":"0"},{"coins":"USD","deputycoin":"BTC","de":"0"},{"coins":"USD","deputycoin":"LTC","de":"0"},{"coins":"USD","deputycoin":"ETH","de":"0"},{"coins":"USD","deputycoin":"ALD","de":"0"},{"coins":"USD","deputycoin":"TIVALUE","de":"0"}]
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
         * coins : CNY
         * deputycoin : BDS
         * de : 1
         */

        private String coins;
        private String deputycoin;
        private String de;//是否默认

        public String getCoins() {
            return coins;
        }

        public void setCoins(String coins) {
            this.coins = coins;
        }

        public String getDeputycoin() {
            return deputycoin;
        }

        public void setDeputycoin(String deputycoin) {
            this.deputycoin = deputycoin;
        }

        public String getDe() {
            return de;
        }

        public void setDe(String de) {
            this.de = de;
        }
    }
}
