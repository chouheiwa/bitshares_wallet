package com.borderless.wallet.socket.chain;

import com.borderless.wallet.socket.account_object;
import com.borderless.wallet.utils.TextUtils;

/**
 * Created by Administrator on 2017/11/24.
 */

public class CallOrder {
    public object_id id;
    public object_id<account_object> borrower;
    public String collateral;
    public String debt;
    public price call_price;
}
