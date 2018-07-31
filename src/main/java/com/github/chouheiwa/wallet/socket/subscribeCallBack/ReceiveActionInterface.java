package com.github.chouheiwa.wallet.socket.subscribeCallBack;

import com.github.chouheiwa.wallet.socket.chain.block_object;

public interface ReceiveActionInterface {
    /**
     * 得到block后的回调
     * @param block_object 区块数据
     */
    public void onReceiveBlockAction(block_object block_object);
}
