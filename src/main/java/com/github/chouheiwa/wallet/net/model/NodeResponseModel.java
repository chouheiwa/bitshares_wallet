package com.github.chouheiwa.wallet.net.model;

import java.util.List;

/**
 * Created by Administrator on 2017/10/9.
 */

public class NodeResponseModel {
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

    public void setData(List<DataBean>  dataBean) {
        this.data = dataBean;
    }

    public static class DataBean {

        private String name;//节点名称
        private String speed;//延迟
        private String status;//状态
        private String selected; //选择

        public DataBean(String name) {
            this.name = name;
        }

        public DataBean(String name, String speed, String status, String selected) {
            this.name = name;
            this.speed = speed;
            this.status = status;
            this.selected = selected;
        }

        public String getName() {


            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpeed() {
            if (null==speed)
                return "0";
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }
    }
}
