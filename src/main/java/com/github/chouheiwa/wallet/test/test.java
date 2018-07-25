package com.github.chouheiwa.wallet.test;

import com.github.chouheiwa.wallet.socket.BitsharesWalletWraper;
import com.github.chouheiwa.wallet.socket.chain.object_id;
import com.github.chouheiwa.wallet.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] ss) {
        BitsharesWalletWraper bww = new BitsharesWalletWraper();

        bww.setWebsocket_server("ws://47.104.82.7:11117");

        try {
            List list1 = bww.get_account_history_operations_with_last_id(object_id.create_from_string("1.2.352674"),0,100,0);

            System.out.println(list1.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
