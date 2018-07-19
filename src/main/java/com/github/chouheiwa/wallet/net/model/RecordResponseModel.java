package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 */

public class RecordResponseModel {
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

    public void setData(List<DataBean>  dataBean) {
        this.data = dataBean;
    }

    public static class DataBean {

        private String CoinType;//最新价格
        private String Type;//币种
        private String Name;
        private String Count;//数量
        private String CreateDate; //创建时间
        private String Status; //状态
        private String AcceptanceID; //承兑商
        private String Householder; // 开户行
        private String BankName;//户名
        private String Account;//卡号
        private String Remark;
        private String Evaluate;
        private String Free;
        private String CashIn_ID;
        private String CashOut_ID;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCashOut_ID() {
            return CashOut_ID;
        }

        public void setCashOut_ID(String cashOut_ID) {
            CashOut_ID = cashOut_ID;
        }

        public String getCashIn_ID() {
            return CashIn_ID;
        }

        public void setCashIn_ID(String cashIn_ID) {
            CashIn_ID = cashIn_ID;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getEvaluate() {
            return Evaluate;
        }

        public void setEvaluate(String evaluate) {
            Evaluate = evaluate;
        }

        public String getFree() {
            return Free;
        }

        public void setFree(String free) {
            Free = free;
        }

        public String getHouseholder() {
            return Householder;
        }

        public void setHouseholder(String householder) {
            Householder = householder;
        }

        public String getBankName() {
            return BankName;
        }

        public void setBankName(String bankName) {
            BankName = bankName;
        }

        public String getAccount() {
            return Account;
        }

        public void setAccount(String account) {
            Account = account;
        }

        public String getCoinType() {
            return CoinType;
        }

        public void setCoinType(String coinType) {
            CoinType = coinType;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String count) {
            Count = count;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String createDate) {
            CreateDate = createDate;
        }


        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getAcceptanceID() {
            return AcceptanceID;
        }

        public void setAcceptanceID(String acceptanceID) {
            AcceptanceID = acceptanceID;
        }
    }
}
