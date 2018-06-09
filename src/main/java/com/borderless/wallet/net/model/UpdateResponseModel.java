package com.borderless.wallet.net.model;

import java.util.List;

/**
 * Created by dagou on 2017/9/25.
 */

public class UpdateResponseModel {

    /**
     * iosversion : 1.0.0
     * andversion : 1.0.0
     * status : ture
     * contents : 1、Add multilingual functionality
     2、Improve user experience
     3、Fixing existing bugs
     * andurl : ["http://www.bejson.com","http://www.bejson.com","http://www.bejson.com"]
     * iosurl : ["http://www.bejson.com","http://www.bejson.com","http://www.bejson.com"]
     */

    private String iosversion;
    private String andversion;
    private String status;
    private String contents;//英文

    private String contents_cn;//簡體中文
    private String contents_tw;//繁體中文
    private List<String> andurl;
    private List<String> iosurl;

    public String getContents_cn() {
        return contents_cn;
    }

    public void setContents_cn(String contents_cn) {
        this.contents_cn = contents_cn;
    }

    public String getContents_tw() {
        return contents_tw;
    }

    public void setContents_tw(String contents_tw) {
        this.contents_tw = contents_tw;
    }

    public String getIosversion() {
        return iosversion;
    }

    public void setIosversion(String iosversion) {
        this.iosversion = iosversion;
    }

    public String getAndversion() {
        return andversion;
    }

    public void setAndversion(String andversion) {
        this.andversion = andversion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<String> getAndurl() {
        return andurl;
    }

    public void setAndurl(List<String> andurl) {
        this.andurl = andurl;
    }

    public List<String> getIosurl() {
        return iosurl;
    }

    public void setIosurl(List<String> iosurl) {
        this.iosurl = iosurl;
    }
}
