package com.borderless.wallet.net.model;

/**
 * Created by 18301 on 2018/1/7.
 */

public class AllHistoryResult {
    private String time; //时间
    private String price;//价格
    private String type;//买卖类型
    private String baseSymbolAmount;//主币数量
    private String qouteSymbolAmount;//从币数量


    public AllHistoryResult(String time, String price, String type, String baseSymbolAmount, String qouteSymbolAmount) {
        this.time = time;
        this.price = price;
        this.type = type;
        this.baseSymbolAmount = baseSymbolAmount;
        this.qouteSymbolAmount = qouteSymbolAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQouteSymbolAmount() {
        return qouteSymbolAmount;
    }

    public void setQouteSymbolAmount(String qouteSymbolAmount) {
        this.qouteSymbolAmount = qouteSymbolAmount;
    }

    public String getBaseSymbolAmount() {
        return baseSymbolAmount;
    }

    public void setBaseSymbolAmount(String baseSymbolAmount) {
        this.baseSymbolAmount = baseSymbolAmount;
    }
}
