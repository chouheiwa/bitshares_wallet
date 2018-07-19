package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */

public class OrderXiangQingModel extends ResponseModelBase {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String orderNo;
        private String memberBdsAccount;
        private String memberName;
        private String acceptantBdsAccount;
        private String acceptantName;
        private String payTypeCode;

        private String payType;
        private String symbol;
        private String amount;

        private String statusCode;
        private String status;
        private String token;

        private String nodeID;
        private String rate;
        private String evaluate;
        private String createDate;



    }
}
