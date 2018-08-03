package com.github.chouheiwa.wallet.test;

import com.github.chouheiwa.wallet.socket.chain.block_object;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveActionInterface;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.WebsocketCallBackApi;
import com.github.chouheiwa.wallet.utils.GsonUtil;

public class test {
    public static WebsocketCallBackApi callBackApi;

    public static void main(String[] ss) {

        callBackApi = new WebsocketCallBackApi() {
            @Override
            public void onErrorException(Exception e) {

            }
        };

        callBackApi.connect("ws://47.104.82.7:11117");

        callBackApi.registerReceive(new ReceiveActionInterface() {
            @Override
            public void onReceiveBlockAction(block_object block_object) {
                System.out.println(GsonUtil.GsonString(block_object));
            }
        });
    }

}
