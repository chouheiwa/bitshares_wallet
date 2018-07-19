package com.github.chouheiwa.wallet.net.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class AcceptanceResponseModel {


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

    public static class DataBean  implements Serializable{

        private String Name;
        private String Evaluate;
        private String way;

        private String AliPayNumber ;
        private String WeChatNumber;
        private String dealcount;
        private String Bond;
        private String PhoneNumber;
        private String Type;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private ArrayList<Result> result;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getEvaluate() {
            return Evaluate;
        }

        public void setEvaluate(String evaluate) {
            Evaluate = evaluate;
        }

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        public String getAliPayNumber() {
            return AliPayNumber;
        }

        public void setAliPayNumber(String aliPayNumber) {
            AliPayNumber = aliPayNumber;
        }

        public String getWeChatNumber() {
            return WeChatNumber;
        }

        public void setWeChatNumber(String weChatNumber) {
            WeChatNumber = weChatNumber;
        }

        public String getDealcount() {
            return dealcount;
        }

        public void setDealcount(String dealcount) {
            this.dealcount = dealcount;
        }

        public String getBond() {
            return Bond;
        }

        public void setBond(String bond) {
            Bond = bond;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            PhoneNumber = phoneNumber;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public ArrayList<Result> getResult() {
            return result;
        }

        public void setResult(ArrayList<Result> result) {
            this.result = result;
        }
    }


    public static class Result implements Serializable{
        private String suname;
        private String loan;
        private String free;
        private String cash;
        private String out;
        private String cashdeal;

        private double outUp;
        private double inUp;
        private String Offline;

        public String getOffline() {
            return Offline;
        }

        public void setOffline(String offline) {
            Offline = offline;
        }

        public double getOutUp() {
            return outUp;
        }

        public void setOutUp(double outUp) {
            this.outUp = outUp;
        }

        public double getInUp() {
            return inUp;
        }

        public void setInUp(double inUp) {
            this.inUp = inUp;
        }

        public String getSuname() {
            return suname;
        }

        public void setSuname(String suname) {
            this.suname = suname;
        }

        public String getLoan() {
            return loan;
        }

        public void setLoan(String loan) {
            this.loan = loan;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getOut() {
            return out;
        }

        public void setOut(String out) {
            this.out = out;
        }

        public String getCashdeal() {
            return cashdeal;
        }

        public void setCashdeal(String cashdeal) {
            this.cashdeal = cashdeal;
        }
    }
}
