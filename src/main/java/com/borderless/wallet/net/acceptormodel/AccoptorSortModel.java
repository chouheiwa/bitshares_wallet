package com.borderless.wallet.net.acceptormodel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class AccoptorSortModel extends ResponseModelBase {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private String sort;
        private String de;
        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getDe() {
            return de;
        }

        public void setDe(String de) {
            this.de = de;
        }
    }
}
