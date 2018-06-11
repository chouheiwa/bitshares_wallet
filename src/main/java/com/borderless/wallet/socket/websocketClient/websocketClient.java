package com.borderless.wallet.socket.websocketClient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class websocketClient extends WebSocketClient {
    private websocketInterface listener;


    public websocketClient(String serverUri,websocketInterface listener) throws URISyntaxException {
        super(new URI(serverUri));
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        listener.onOpen();
    }

    @Override
    public void onMessage(String s) {
        listener.onMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {
        listener.onError(e);
    }
}
