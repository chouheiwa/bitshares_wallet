package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/17.
 */

public class AcceptorOpenModel extends ResponseModelBase {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String bdsaccountco;

        public String getBdsaccountCo() {
            return bdsaccountco;
        }

        public void setBdsaccountCo(String bdsAccountCo) {
            this.bdsaccountco = bdsAccountCo;
        }
    }
}
