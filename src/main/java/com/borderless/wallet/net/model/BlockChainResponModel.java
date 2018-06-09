package com.borderless.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/22.
 */

public class BlockChainResponModel {


    /**
     * status : success
     * msg :
     * data : [{"head_block_num":200451,"head_block_id":"00030f038bc373b45353ea7de29f6b4db111dc84","head_block_age":"4 seconds old","next_maintenance_time":"22 hours in the future","chain_id":"178c044c62ada670e29e820a2e220bec1d4cdf3630d50a569718a2f4f3ba7970","participation":"91.40625000000000000","active_witnesses":["1.6.1","1.6.2","1.6.3","1.6.4","1.6.5","1.6.6","1.6.7","1.6.8","1.6.9","1.6.10","1.6.11"],"active_committee_members":["1.5.0","1.5.1","1.5.2","1.5.3","1.5.4","1.5.5","1.5.6","1.5.7","1.5.8","1.5.9","1.5.10"]}]
     */

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

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * head_block_num : 200451
         * head_block_id : 00030f038bc373b45353ea7de29f6b4db111dc84
         * head_block_age : 4 seconds old
         * next_maintenance_time : 22 hours in the future
         * chain_id : 178c044c62ada670e29e820a2e220bec1d4cdf3630d50a569718a2f4f3ba7970
         * participation : 91.40625000000000000
         * active_witnesses : ["1.6.1","1.6.2","1.6.3","1.6.4","1.6.5","1.6.6","1.6.7","1.6.8","1.6.9","1.6.10","1.6.11"]
         * active_committee_members : ["1.5.0","1.5.1","1.5.2","1.5.3","1.5.4","1.5.5","1.5.6","1.5.7","1.5.8","1.5.9","1.5.10"]
         */

        private int head_block_num;
        private String head_block_id;
        private String head_block_age;
        private String next_maintenance_time;
        private String chain_id;
        private String participation;
        private List<String> active_witnesses;
        private List<String> active_committee_members;

        public int getHead_block_num() {
            return head_block_num;
        }

        public void setHead_block_num(int head_block_num) {
            this.head_block_num = head_block_num;
        }

        public String getHead_block_id() {
            return head_block_id;
        }

        public void setHead_block_id(String head_block_id) {
            this.head_block_id = head_block_id;
        }

        public String getHead_block_age() {
            return head_block_age;
        }

        public void setHead_block_age(String head_block_age) {
            this.head_block_age = head_block_age;
        }

        public String getNext_maintenance_time() {
            return next_maintenance_time;
        }

        public void setNext_maintenance_time(String next_maintenance_time) {
            this.next_maintenance_time = next_maintenance_time;
        }

        public String getChain_id() {
            return chain_id;
        }

        public void setChain_id(String chain_id) {
            this.chain_id = chain_id;
        }

        public String getParticipation() {
            return participation;
        }

        public void setParticipation(String participation) {
            this.participation = participation;
        }

        public List<String> getActive_witnesses() {
            return active_witnesses;
        }

        public void setActive_witnesses(List<String> active_witnesses) {
            this.active_witnesses = active_witnesses;
        }

        public List<String> getActive_committee_members() {
            return active_committee_members;
        }

        public void setActive_committee_members(List<String> active_committee_members) {
            this.active_committee_members = active_committee_members;
        }
    }
}
