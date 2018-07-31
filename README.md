# bitshares_wallet
A bitshares-wallet for java
I get it from an Android application in [this page](https://github.com/bitshares/bitshares_andriod_wallet).

And I mixed some question.

It can be used in Java Server such as Spring.

If you want to import this application.

It can be used in maven.

Simple usage:

Now the latest version is 2.1.0

If you want to use it in your project,you should

1. Add dependency to your pom.xml
```
<dependency>
            <groupId>com.github.chouheiwa</groupId>
            <artifactId>wallet</artifactId>
            <version>2.1.0</version>
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
WebsocketCallBackApi callBackApi = new WebsocketCallBackApi();
//var m is the result of the connection.
Boolean m = callBackApi.setWebsocket_server("ws://192.168.1.1:8080");

callBackApi.registerReceive(new ReceiveActionInterface() {
//This function give the latest block_object when the first time network connected.
     @Override
     public void onReceiveBlockAction(block_object block_object) {
     //Do something you want to do when you recevie new block.
     //The block_object.transactions was the detail of the block's operation such as transfer,limit order
        System.out.println(GsonUtil.GsonString(block_object));
     }
});

```