package com.github.chouheiwa.wallet.net.model;

/**
 * Created by admin on 2017/12/8.
 */

public class RechargeResponseModel {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RechargeResponseModel(String key, String value) {

        this.key = key;
        this.value = value;
    }
}
