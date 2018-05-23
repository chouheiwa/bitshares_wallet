package com.borderless.wallet.socket.chain;

import de.bitsharesmunich.graphenej.LimitOrder;

/**
 * Created by daihongwei on 2017/10/25.
 */


public class FullAccountOrder {

    public LimitOrder limitOrder;
    public asset_object base;
    public asset_object quote;
    public double price;
}
