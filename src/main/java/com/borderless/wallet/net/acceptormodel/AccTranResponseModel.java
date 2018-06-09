package com.borderless.wallet.net.acceptormodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AccTranResponseModel extends ResponseModelBase {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean  implements Serializable {
        private String orderNo;
        private String memberName;
        private String memberBdsAccount;
        private String acceptantName;
        private String amount;
        private String symbol;
        private String status;
        private String payStyleID;
        private String id;
        private String orderTypeCode;
        private String orderStatusCode;
        private String orderStatus;
        private String appealStateCode;
        private String appealState;
        private String payTypeCode;
        private String payType;
        private String createDate;
        private String orderType;
        private String dealCode;
        private String statusCode;
        private String fee;
        private String attitudeEvaluation;
        private String speedEvaluation;
        private String honestEvaluation;
        private String rate;

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getAcceptantName() {
            return acceptantName;
        }

        public void setAcceptantName(String acceptantName) {
            this.acceptantName = acceptantName;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPayStyleID() {
            return payStyleID;
        }

        public void setPayStyleID(String payStyleID) {
            this.payStyleID = payStyleID;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getDealCode() {
            return dealCode;
        }

        public void setDealCode(String dealCode) {
            this.dealCode = dealCode;
        }

        private String acceptantBdsAccount;

        public String getAcceptantBdsAccount() {
            return acceptantBdsAccount;
        }

        public void setAcceptantBdsAccount(String acceptantBdsAccount) {
            this.acceptantBdsAccount = acceptantBdsAccount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberBdsAccount() {
            return memberBdsAccount;
        }

        public void setMemberBdsAccount(String memberBdsAccount) {
            this.memberBdsAccount = memberBdsAccount;
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

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getOrderTypeCode() {
            return orderTypeCode;
        }

        public void setOrderTypeCode(String orderTypeCode) {
            this.orderTypeCode = orderTypeCode;
        }

        public String getOrderStatusCode() {
            return orderStatusCode;
        }

        public void setOrderStatusCode(String orderStatusCode) {
            this.orderStatusCode = orderStatusCode;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getAppealStateCode() {
            return appealStateCode;
        }

        public void setAppealStateCode(String appealStateCode) {
            this.appealStateCode = appealStateCode;
        }

        public String getAppealState() {
            return appealState;
        }

        public void setAppealState(String appealState) {
            this.appealState = appealState;
        }

        public String getPayTypeCode() {
            return payTypeCode;
        }

        public void setPayTypeCode(String payTypeCode) {
            this.payTypeCode = payTypeCode;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }


        public String getAttitudeEvaluation() {
            return attitudeEvaluation;
        }

        public void setAttitudeEvaluation(String attitudeEvaluation) {
            this.attitudeEvaluation = attitudeEvaluation;
        }

        public String getSpeedEvaluation() {
            return speedEvaluation;
        }

        public void setSpeedEvaluation(String speedEvaluation) {
            this.speedEvaluation = speedEvaluation;
        }

        public String getHonestEvaluation() {
            return honestEvaluation;
        }

        public void setHonestEvaluation(String honestEvaluation) {
            this.honestEvaluation = honestEvaluation;
        }
    }

}
