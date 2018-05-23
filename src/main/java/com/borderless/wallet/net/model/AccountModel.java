package com.borderless.wallet.net.model;

/**
 * Created by Administrator on 2017/10/19.
 */

public class AccountModel {

    private String name;
    private String id;
    private String memokey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemokey() {
        return memokey;
    }

    public void setMemokey(String memokey) {
        this.memokey = memokey;
    }
}
