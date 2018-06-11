package com.borderless.wallet.test;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketTest extends WebSocketClient {
    public WebsocketTest(URI serverUri) {
        super(serverUri);
    }

    public static  void  main(String[] args) {
        try {
            WebsocketTest test =  new WebsocketTest(new URI("ws://47.98.234.136:1234"));
            test.connect();
//            synchronized (test) {
//                if (!test.getReadyState().equals(READYSTATE.OPEN)) {
//                    try {
//                        test.wait(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("开始握手");
        send("{\"method\":\"call\",\"params\":[1,\"login\",[\"\",\"\"]],\"id\":1}");
    }

    @Override
    public void onMessage(String s) {
        System.out.println(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println(s);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();;
    }
}
