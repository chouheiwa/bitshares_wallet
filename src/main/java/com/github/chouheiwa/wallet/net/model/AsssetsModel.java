package com.github.chouheiwa.wallet.net.model;

/**
 * Created by Administrator on 2018/1/5.
 */

public class AsssetsModel {
    private String symbol;//当前币种
    private String base;//基础币种
    private String latest;//最新价格
    public String precision;//

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    //    private String lowest_ask; //最低卖出价
//    private String highest_bid;//最高买入价
//    private String base_volume;//基础资产数量
//    private String quote_volume; //报价资产数量
    private String per;//跌涨幅
    private String amount;//持有的货币数量

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
