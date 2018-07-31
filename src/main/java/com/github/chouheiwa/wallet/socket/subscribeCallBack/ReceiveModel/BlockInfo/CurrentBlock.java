package com.github.chouheiwa.wallet.socket.subscribeCallBack.ReceiveModel.BlockInfo;

import com.github.chouheiwa.wallet.socket.chain.object_id;
import com.google.common.primitives.UnsignedLong;
import com.google.common.primitives.UnsignedLongs;

import java.util.Date;

public class CurrentBlock {
    public object_id id;
    /**
     * 当前最新区块
     */
    public Long head_block_number;

    public String head_block_id;

    public String time;

    public object_id current_witness;

    public String next_maintenance_time;

    public String last_budget_time;

    public Long witness_budget;

    public Integer accounts_registered_this_interval;

    public Integer recently_missed_count;

    public Long current_aslot;

    public String recent_slots_filled;

    public Integer dynamic_flags;
    /**
     * 当前最新不可变区块
     */
    public Long last_irreversible_block_num;
}
