package com.github.chouheiwa.wallet.socket.subscribeCallBack;

import com.google.gson.JsonElement;

import java.util.List;

public abstract class CallBackUploadAbstractModel {
    public enum UploadKind {
        uploadKindBase,
        uploadKindDataBase
    }

    private CallBackUploadModel callBackUploadModel = new CallBackUploadModel();

    private boolean onlyCall;

    private UploadKind uploadKind;

    private String callMethod;

    private List<Object> callParams;

    public CallBackUploadAbstractModel(String callMethod,List<Object> callParams) {
        this.onlyCall = true;
        this.uploadKind = UploadKind.uploadKindBase;
        this.callMethod = callMethod;
        this.callParams = callParams;
    }

    public CallBackUploadAbstractModel(boolean onlyCall,UploadKind uploadKind,String callMethod,List<Object> callParams) {
        this.onlyCall = onlyCall;
        this.uploadKind = uploadKind;
        this.callMethod = callMethod;
        this.callParams = callParams;
    }

    public boolean isOnlyCall() {
        return onlyCall;
    }

    public CallBackUploadModel getCallBackUploadModel() {
        return callBackUploadModel;
    }

    public UploadKind getUploadKind() {
        return uploadKind;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public List<Object> getCallParams() {
        return callParams;
    }

    public abstract void reciveCallMessage(JsonElement result);

    public abstract void reciveNoticeMessage(JsonElement result);
}
