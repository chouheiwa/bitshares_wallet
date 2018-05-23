package com.borderless.wallet.net.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class AccountResponseModel {

    /**
     * status : success
     * msg :
     * data : [{"amount":39522118,"asset_id":"1.3.0"}]
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

    public static class DataBean implements Serializable {
        /**
         * amount : 39522118
         * asset_id : 1.3.0
         *
         */

        private String payStyleID;
        private String symbol;
        private String typeCode;
        private String name;
        private String type;
        private String bank;
        private String payAccountID;
        private String swiftBic;
        private String isAcceptantPay;
        private String createDate;
        private String payCode;

        public String getPayStyleID() {
            return payStyleID;
        }

        public void setPayStyleID(String payStyleID) {
            this.payStyleID = payStyleID;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getPayAccountID() {
            return payAccountID;
        }

        public void setPayAccountID(String payAccountID) {
            this.payAccountID = payAccountID;
        }

        public String getSwiftBic() {
            return swiftBic;
        }

        public void setSwiftBic(String swiftBic) {
            this.swiftBic = swiftBic;
        }

        public String getIsAcceptantPay() {
            return isAcceptantPay;
        }

        public void setIsAcceptantPay(String isAcceptantPay) {
            this.isAcceptantPay = isAcceptantPay;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getPayCode() {
            return payCode;
        }

        public void setPayCode(String payCode) {
            this.payCode = payCode;
        }
    }
}
