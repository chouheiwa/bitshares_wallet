package com.github.chouheiwa.wallet.socket.chain;


public class operation_history_object {
    public String id;
    public operations.operation_type op;
    public int block_num;
    public int trx_in_block;
    public int op_in_trx;
    public int virtual_op;
    public String description = "";
    public String memo = "";
}
