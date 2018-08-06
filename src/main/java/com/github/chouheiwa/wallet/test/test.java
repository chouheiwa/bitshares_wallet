package com.github.chouheiwa.wallet.test;

import com.github.chouheiwa.wallet.socket.chain.block_object;
import com.github.chouheiwa.wallet.socket.chain.operations;
import com.github.chouheiwa.wallet.socket.chain.transaction;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveActionInterface;
import com.github.chouheiwa.wallet.socket.subscribeCallBack.WebsocketCallBackApi;
import com.github.chouheiwa.wallet.utils.GsonUtil;

public class test {
    public static WebsocketCallBackApi callBackApi;
    public static void main(String[] ss) {
        //初始化抽象类时需要发布异常处理
        callBackApi = new WebsocketCallBackApi() {
            @Override
            public void onErrorException(Exception e) {

            }
        };

        callBackApi.connect("ws://127.0.0.1:8056");

        callBackApi.registerReceive(new ReceiveActionInterface() {
            @Override
            public void onReceiveBlockAction(block_object block_object) {
                //收到最新区块后获取最新区块对象
                /**
                 * 这里将区块链对象反回了操作
                 */

//                for (transaction transaction: block_object.transactions) {
//                    for (operations.operation_type operation_type :transaction.operations) {
//                        if (operation_type.nOperationType == 1) {
//                            operations.limit_order_create_operation limitOrderCreateOperation = (operations.limit_order_create_operation) operation_type.operationContent;
//                        }
//                    }
//                }
            }
        });
    }

}
