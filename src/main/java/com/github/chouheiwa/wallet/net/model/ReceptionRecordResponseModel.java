package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/21.
 */

public class ReceptionRecordResponseModel {

    /**
     * status : success
     * msg :
     * data :
     */

    private String status;
    private String msg;
    private List<NewObject> data;

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

    public List<NewObject> getData() {
        return data;
    }

    public void setData(List<NewObject> data) {
        this.data = data;
    }


    public class NewObject {

        String amount;
        String symbol;
        String from;
        String to;

        public String getTo() {
            return to;
        }

        public String getFrom() {
            return from;
        }

        public String getAmount() {
            return amount;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
