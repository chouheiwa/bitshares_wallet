package com.borderless.wallet.net.model;

/**
 * Created by Administrator on 2017/10/9.
 */

public class ExchangeResponseModel {
    private String status;
    private String msg;
    private DataBean dataBean;

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

    public DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public static class DataBean {
        private String quote;//当前币种
        private String base;//基础币种
        private String latest;//最新价格
        private String lowest_ask; //最低卖出价
        private String  highest_bid;//最高买入价
        private String  base_volume;//基础资产数量
        private String quote_volume; //报价资产数量
        private  String per;//跌涨幅

        public String getPer() {
            return per;
        }

        public void setPer(String per) {
            this.per = per;
        }

        public String getTurnover() {
            return turnover;
        }

        public void setTurnover(String turnover) {
            this.turnover = turnover;
        }

        /**
         * turnover = latest * quote_volume
         */

        private String turnover;//成交额

        public String getQuote() {
            return quote;
        }

        public void setQuote(String quote) {
            this.quote = quote;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getLatest() {
            return latest;
        }

        public void setLatest(String latest) {
            this.latest = latest;
        }

        public String getLowest_ask() {
            return lowest_ask;
        }

        public void setLowest_ask(String lowest_ask) {
            this.lowest_ask = lowest_ask;
        }

        public String getHighest_bid() {
            return highest_bid;
        }

        public void setHighest_bid(String highest_bid) {
            this.highest_bid = highest_bid;
        }

        public String getBase_volume() {
            return base_volume;
        }

        public void setBase_volume(String base_volume) {
            this.base_volume = base_volume;
        }

        public String getQuote_volume() {
            return quote_volume;
        }

        public void setQuote_volume(String quote_volume) {
            this.quote_volume = quote_volume;
        }
    }

}
