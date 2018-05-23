package com.borderless.wallet.socket.market;


import java.util.List;


public class OrderBook {
    public String base;
    public String quote;
    public List<Order> bids; //卖
    public List<Order> asks; //买
}
