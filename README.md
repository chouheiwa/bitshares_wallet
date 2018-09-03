# bitshares_wallet
A bitshares-wallet for java
I get it from an Android application in [this page](https://github.com/bitshares/bitshares_andriod_wallet).

And I mixed some question.

It can be used in Java Server such as Spring.

If you want to import this application.

It can be used in maven.

Simple usage:

Now the latest version is 2.1.15

If you want to use it in your project,you should

1. Add dependency to your pom.xml
```
<dependency>
            <groupId>com.github.chouheiwa</groupId>
            <artifactId>wallet</artifactId>
            <version>2.1.15</version>
</dependency>
```
2. Use class BitsharesWalletWrapper
```
BitsharesWalletWraper walletWraper = new BitsharesWalletWraper();
// Here has another constructor function
// You should put the chain_id's sha256 and the prefix of publickey in the chain into this function
Boolean m = walletWraper.setWebsocket_server("ws://192.168.1.1:8080");
// var m is the result of the connection.
//If m == false,you can not do anything,it may be the witness_node address was wrong.
//Next step is likly cli_wallet for bitshres
```
I add new class to subcribe function to block chain.

Use class **WebsocketCallBackApi**
```
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
```