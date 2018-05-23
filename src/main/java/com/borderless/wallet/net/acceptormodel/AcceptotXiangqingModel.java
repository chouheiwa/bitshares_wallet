package com.borderless.wallet.net.acceptormodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */

public class AcceptotXiangqingModel extends ResponseModelBase{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {


        /*
        totalEvaluate Float 综合评价        attitudeEvaluate Float         Float         Float


        * */
        private String acceptantBdsAccount;
        private String acceptantName;
        private String score;//评分
        private String introduce;//introduce
        private String mortgage;
        private String symbol;
        private String statusCode;//承兑商状态代码
        private String status;//承兑商状态
        private String bdsAccountCo;///承兑商对公账户

        private String cashInUpperLimit;//充值上限
        private String cashOutUpperLimit;//提现上限
        private String cashInLowerLimit;//充值下限
        private String cashOutLowerLimit;//提现下限

        private String cashInServiceRate;//充值网关手续费率
        private String cashOutServiceRate;
        private String cashInRate;//充值手续费率
        private String cashOutRate;
        private String cashInSupportPayType;//充值支持付款方式
        private String cashOutSupportPayType;

        private String cashInLock;//cashInLock充值订单锁定金额
        private String cashOutLock;

        private String cashOutPerBalance;//单笔提现额度
        private String cashOutBalance;//提现额度

        private String bsdcopubkey;//承兑商对公账户公钥

        private String acceptLimit;//承兑额度

        private String accountBalance;//账户余额

        private String dealCount;//完成订单数
        private String dealAmount;//王成订单金额


        private String cashInUncompletedCount;//未完成充值订单数
        private String cashOutUncompletedCount;//未完成充值订单金额
        private String cashInUncompletedAmount;
        private String cashOutUncompletedAmount;

        private String totalEvaluation;//综合评价
        private String createDate;
        private String mobile;
        private String onlineStartTime;
        private String onlineEndTime;
        private String onlineTimeState;
        private String isOnline;

        public String getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(String isOnline) {
            this.isOnline = isOnline;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAcceptantBdsAccount() {
            return acceptantBdsAccount;
        }

        public void setAcceptantBdsAccount(String acceptantBdsAccount) {
            this.acceptantBdsAccount = acceptantBdsAccount;
        }

        public String getAcceptantName() {
            return acceptantName;
        }

        public void setAcceptantName(String acceptantName) {
            this.acceptantName = acceptantName;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getMortgage() {
            return mortgage;
        }

        public void setMortgage(String mortgage) {
            this.mortgage = mortgage;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBdsAccountCo() {
            return bdsAccountCo;
        }

        public void setBdsAccountCo(String bdsAccountCo) {
            this.bdsAccountCo = bdsAccountCo;
        }

        public String getCashInUpperLimit() {
            return cashInUpperLimit;
        }

        public void setCashInUpperLimit(String cashInUpperLimit) {
            this.cashInUpperLimit = cashInUpperLimit;
        }

        public String getCashOutUpperLimit() {
            return cashOutUpperLimit;
        }

        public void setCashOutUpperLimit(String cashOutUpperLimit) {
            this.cashOutUpperLimit = cashOutUpperLimit;
        }

        public String getCashInLowerLimit() {
            return cashInLowerLimit;
        }

        public void setCashInLowerLimit(String cashInLowerLimit) {
            this.cashInLowerLimit = cashInLowerLimit;
        }

        public String getCashOutLowerLimit() {
            return cashOutLowerLimit;
        }

        public void setCashOutLowerLimit(String cashOutLowerLimit) {
            this.cashOutLowerLimit = cashOutLowerLimit;
        }

        public String getCashInServiceRate() {
            return cashInServiceRate;
        }

        public void setCashInServiceRate(String cashInServiceRate) {
            this.cashInServiceRate = cashInServiceRate;
        }

        public String getCashOutServiceRate() {
            return cashOutServiceRate;
        }

        public void setCashOutServiceRate(String cashOutServiceRate) {
            this.cashOutServiceRate = cashOutServiceRate;
        }

        public String getCashInRate() {
            return cashInRate;
        }

        public void setCashInRate(String cashInRate) {
            this.cashInRate = cashInRate;
        }

        public String getCashOutRate() {
            return cashOutRate;
        }

        public void setCashOutRate(String cashOutRate) {
            this.cashOutRate = cashOutRate;
        }

        public String getCashInSupportPayType() {
            return cashInSupportPayType;
        }

        public void setCashInSupportPayType(String cashInSupportPayType) {
            this.cashInSupportPayType = cashInSupportPayType;
        }

        public String getCashOutSupportPayType() {
            return cashOutSupportPayType;
        }

        public void setCashOutSupportPayType(String cashOutSupportPayType) {
            this.cashOutSupportPayType = cashOutSupportPayType;
        }

        public String getCashInLock() {
            return cashInLock;
        }

        public void setCashInLock(String cashInLock) {
            this.cashInLock = cashInLock;
        }

        public String getCashOutLock() {
            return cashOutLock;
        }

        public void setCashOutLock(String cashOutLock) {
            this.cashOutLock = cashOutLock;
        }

        public String getCashOutPerBalance() {
            return cashOutPerBalance;
        }

        public void setCashOutPerBalance(String cashOutPerBalance) {
            this.cashOutPerBalance = cashOutPerBalance;
        }

        public String getCashOutBalance() {
            return cashOutBalance;
        }

        public void setCashOutBalance(String cashOutBalance) {
            this.cashOutBalance = cashOutBalance;
        }

        public String getBsdcopubkey() {
            return bsdcopubkey;
        }

        public void setBsdcopubkey(String bsdcopubkey) {
            this.bsdcopubkey = bsdcopubkey;
        }

        public String getAcceptLimit() {
            return acceptLimit;
        }

        public void setAcceptLimit(String acceptLimit) {
            this.acceptLimit = acceptLimit;
        }

        public String getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(String accountBalance) {
            this.accountBalance = accountBalance;
        }

        public String getCashInUncompletedCount() {
            return cashInUncompletedCount;
        }

        public void setCashInUncompletedCount(String cashInUncompletedCount) {
            this.cashInUncompletedCount = cashInUncompletedCount;
        }

        public String getCashOutUncompletedCount() {
            return cashOutUncompletedCount;
        }

        public void setCashOutUncompletedCount(String cashOutUncompletedCount) {
            this.cashOutUncompletedCount = cashOutUncompletedCount;
        }

        public String getCashInUncompletedAmount() {
            return cashInUncompletedAmount;
        }

        public void setCashInUncompletedAmount(String cashInUncompletedAmount) {
            this.cashInUncompletedAmount = cashInUncompletedAmount;
        }

        public String getCashOutUncompletedAmount() {
            return cashOutUncompletedAmount;
        }

        public void setCashOutUncompletedAmount(String cashOutUncompletedAmount) {
            this.cashOutUncompletedAmount = cashOutUncompletedAmount;
        }

        public String getDealCount() {
            return dealCount;
        }

        public void setDealCount(String dealCount) {
            this.dealCount = dealCount;
        }

        public String getDealAmount() {
            return dealAmount;
        }

        public void setDealAmount(String dealAmount) {
            this.dealAmount = dealAmount;
        }

        public String getTotalEvaluation() {
            return totalEvaluation;
        }

        public void setTotalEvaluation(String totalEvaluation) {
            this.totalEvaluation = totalEvaluation;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }


        public String getOnlineStartTime() {
            return onlineStartTime;
        }

        public void setOnlineStartTime(String onlineStartTime) {
            this.onlineStartTime = onlineStartTime;
        }

        public String getOnlineEndTime() {
            return onlineEndTime;
        }

        public void setOnlineEndTime(String onlineEndTime) {
            this.onlineEndTime = onlineEndTime;
        }

        public String getOnlineTimeState() {
            return onlineTimeState;
        }

        public void setOnlineTimeState(String onlineTimeState) {
            this.onlineTimeState = onlineTimeState;
        }
    }
}
