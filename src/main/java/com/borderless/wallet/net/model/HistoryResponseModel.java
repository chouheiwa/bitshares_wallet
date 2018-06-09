package com.borderless.wallet.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class HistoryResponseModel {

    /**
     * status : success
     * msg :
     * data : [{"amount":"10","symbol":"BTS","from":"lee","to":"init0"},{"amount":"10","symbol":"BTS","from":"lee","to":"init0"},{"amount":"500","symbol":"BTS","from":"init0","to":"lee"}]
     *
     * {"status":"success","msg":"","data":[{"amount":"0.56000","symbol":"BTS","from":"lee","to":"init0"},{"amount":"0.00010","symbol":"BTS","from":"lee","to":"init0"},{"amount":"10","symbol":"BTS","from":"lee","to":"init0"},{"amount":"10","symbol":"BTS","from":"lee","to":"init0"},{"amount":"500","symbol":"BTS","from":"init0","to":"lee"}]}
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

    public static class DataBean implements Serializable{
        /**
         * amount : 10
         * symbol : BTS
         * from : lee
         * to : init0
         */

        private String amount;
        private String symbol;
        private String from;
        private String to;
        private String id;
        private String fee;
        private String blockNum;
        private String time;
        private String index;
        private String memo;
        private String exchangeId;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getExchangeId() {
            return exchangeId;
        }

        public void setExchangeId(String exchangeId) {
            this.exchangeId = exchangeId;
        }

        public String getBlockNum() {
            return blockNum;
        }

        public void setBlockNum(String blockNum) {
            this.blockNum = blockNum;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee+" "+symbol;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}
