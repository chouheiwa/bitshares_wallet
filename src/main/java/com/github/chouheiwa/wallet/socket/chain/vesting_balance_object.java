package com.github.chouheiwa.wallet.socket.chain;

/**
 * Created by daihongwei on 2017/11/7.
 */

public class vesting_balance_object {
  public object_id<vesting_balance_object> id; //id
  public String owner;
  public String Available_to_claim;//可领取余额
  public String AvailablePersent;//可领取百分比
  public String TotalBalance;//返现
  public String vesting_seconds;
  public long coin_seconds_earned;


  public String coin_days_earned;//已完成币天
  public String coin_days_required;//要求的币天
  public String vesting_period;//剩余解冻天数
}
