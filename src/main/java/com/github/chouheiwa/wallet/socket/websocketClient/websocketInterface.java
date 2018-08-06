package com.github.chouheiwa.wallet.socket.websocketClient;

public interface websocketInterface {
    public void onOpen();
    public void onMessage(String resultMsg);
    public void onError(Exception e);
    public void onClose(int i, String s, boolean b);
}
