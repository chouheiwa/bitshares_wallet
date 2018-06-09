package com.borderless.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class AccDiYaModel extends ResponseModelBase {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
       private String mortgageRate;
       private String mortgageLowerLimit;

        public String getMortgageLowerLimit() {
            return mortgageLowerLimit;
        }

        public void setMortgageLowerLimit(String bondLowerLimit) {
            this.mortgageLowerLimit = bondLowerLimit;
        }

        public String getMortgageRate() {
            return mortgageRate;
        }

        public void setMortgageRate(String mortgageRate) {
            this.mortgageRate = mortgageRate;
        }
    }
}
