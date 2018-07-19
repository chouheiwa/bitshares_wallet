package com.github.chouheiwa.wallet.socket.chain;

import com.github.chouheiwa.wallet.socket.account_object;
import com.github.chouheiwa.wallet.utils.TextUtils;

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
