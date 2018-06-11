package com.borderless.wallet.socket.websocketClient;

public interface websocketInterface {
    public void onOpen();
    public void onMessage(String resultMsg);
    public void onError(Exception e);

}
