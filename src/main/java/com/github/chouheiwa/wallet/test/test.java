package com.github.chouheiwa.wallet.test;

import com.github.chouheiwa.wallet.socket.BitsharesWalletWraper;
import com.github.chouheiwa.wallet.socket.chain.block_object;
import com.github.chouheiwa.wallet.socket.chain.global_config_object;
import com.github.chouheiwa.wallet.socket.chain.object_id;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.CallBackUploadAbstractModel;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveActionInterface;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveModel.BlockInfo.CurrentBlock;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.WebsocketCallBackApi;
import com.github.chouheiwa.wallet.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static WebsocketCallBackApi callBackApi = new WebsocketCallBackApi();

    public static void main(String[] ss) {
        callBackApi.connect("ws://47.104.82.7:11117");

        callBackApi.registerReceive(new ReceiveActionInterface() {
            @Override
            public void onReceiveBlockAction(block_object block_object) {
                System.out.println(GsonUtil.GsonString(block_object));
            }
        });
    }

}
