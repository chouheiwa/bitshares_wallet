# bitshares_wallet
A bitshares-wallet for java
I get it from an Android application in [this page](https://github.com/bitshares/bitshares_andriod_wallet).

And I mixed some question.

It can be used in Java Server such as Sping.

If you want to import this application.

It can be used in maven.

Simple usage:

Now the lastest version is 2.0

If you want to use it in your project,you should

1. Add dependency to your pom.xml
```
<dependency>
            <groupId>com.github.chouheiwa</groupId>
            <artifactId>wallet</artifactId>
            <version>2.0</version>
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
