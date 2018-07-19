package com.github.chouheiwa.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */

public class AccetorPingjiaModel extends ResponseModelBase {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String totalEvaluate;//综合评价
        private String attitudeEvaluation;//服务态度评价

        private String honestEvaluation;//响应速度评价
        private String speedEvaluation;//诚信度评价

        public String getTotalEvaluate() {
            return totalEvaluate;
        }

        public void setTotalEvaluate(String totalEvaluate) {
            this.totalEvaluate = totalEvaluate;
        }

        public String getAttitudeEvaluation() {
            return attitudeEvaluation;
        }

        public void setAttitudeEvaluation(String attitudeEvaluation) {
            this.attitudeEvaluation = attitudeEvaluation;
        }

        public String getSpeedEvaluation() {
            return speedEvaluation;
        }

        public void setSpeedEvaluation(String speedEvaluation) {
            this.speedEvaluation = speedEvaluation;
        }

        public String getHonestEvaluation() {
            return honestEvaluation;
        }

        public void setHonestEvaluation(String honestEvaluation) {
            this.honestEvaluation = honestEvaluation;
        }
    }
}
