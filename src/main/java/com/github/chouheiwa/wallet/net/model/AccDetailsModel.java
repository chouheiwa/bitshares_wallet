package com.github.chouheiwa.wallet.net.model;

/**
 * Created by Administrator on 2017/11/2.
 */

public class AccDetailsModel {

    private String key;
    private String value;


    public AccDetailsModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

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
}

