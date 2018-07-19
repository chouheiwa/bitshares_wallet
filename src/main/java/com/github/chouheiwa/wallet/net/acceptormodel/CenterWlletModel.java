package com.github.chouheiwa.wallet.net.acceptormodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/29.
 */

public class CenterWlletModel extends ResponseModelBase {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String AccDigitalList_ID;
        private String Type;
        private String BdsAccount;
        private String PublicKey;
        private String PrivateKey;
        private String Rate;
        private String MinAmount;
        private String MaxAmount;
        private String Status;
        private String Deleted;
        private String Address;

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getAccDigitalList_ID() {
            return AccDigitalList_ID;
        }

        public void setAccDigitalList_ID(String accDigitalList_ID) {
            AccDigitalList_ID = accDigitalList_ID;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getBdsAccount() {
            return BdsAccount;
        }

        public void setBdsAccount(String bdsAccount) {
            BdsAccount = bdsAccount;
        }

        public String getPublicKey() {
            return PublicKey;
        }

        public void setPublicKey(String publicKey) {
            PublicKey = publicKey;
        }

        public String getPrivateKey() {
            return PrivateKey;
        }

        public void setPrivateKey(String privateKey) {
            PrivateKey = privateKey;
        }

        public String getRate() {
            return Rate;
        }

        public void setRate(String rate) {
            Rate = rate;
        }

        public String getMinAmount() {
            return MinAmount;
        }

        public void setMinAmount(String minAmount) {
            MinAmount = minAmount;
        }

        public String getMaxAmount() {
            return MaxAmount;
        }

        public void setMaxAmount(String maxAmount) {
            MaxAmount = maxAmount;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getDeleted() {
            return Deleted;
        }

        public void setDeleted(String deleted) {
            Deleted = deleted;
        }
    }

}
