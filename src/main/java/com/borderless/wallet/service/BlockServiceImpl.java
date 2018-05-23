package com.borderless.wallet.service;

import com.borderless.wallet.bo.BlockBuyBo;
import com.borderless.wallet.bo.BlockSellBo;
import com.borderless.wallet.constants.OwnerInfoConstant;
import com.borderless.wallet.net.model.HistoryResponseModel;
import com.borderless.wallet.socket.BitsharesWalletWraper;
import com.borderless.wallet.socket.account_object;
import com.borderless.wallet.socket.chain.limit_order_object;
import com.borderless.wallet.socket.chain.object_id;
import com.borderless.wallet.socket.chain.signed_transaction;
import com.borderless.wallet.socket.exception.NetworkStatusException;
import com.borderless.wallet.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 区块连接服务
 */
@Component
@Service
public class BlockServiceImpl {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    /**
     * 依据用户区块名称查询用户区块信息
     */
    @Async("bitshareWalletWraper")
    public account_object blockUserInfo(String userBlockName){
        log.info("查询用户区块信息开始: 用户"+userBlockName);
        long startTime = System.currentTimeMillis();
        BitsharesWalletWraper bww = new BitsharesWalletWraper();
        account_object accountObject = new account_object();
        try{
            accountObject = bww.get_account_object(userBlockName);
        }catch (Exception e){
            e.printStackTrace();
            bww.close();
            log.error("查询用户"+userBlockName+"区块账户信息异常:",e.getStackTrace());
        }
        long endTime = System.currentTimeMillis();

        log.info(" 用时: "+(endTime - startTime));
        log.info("查询用户区块信息结束: 用户"+userBlockName);
        bww.close();
        return accountObject;
    }

    /**
     * 承兑交易，区块账户转账
     * @param fromUserName
     * @param toUserName
     * @param amountStr
     * @param assetSymbol
     * @param strMemo
     * @param amountToFee
     * @param symbolToFee
     */
    @Async("bitshareWalletWraper")
    public signed_transaction acceptorTransfer(String fromUserName ,String toUserName ,String amountStr ,String assetSymbol ,
                                 String strMemo, String amountToFee ,String symbolToFee,String memoInfo){
        BitsharesWalletWraper bww = new  BitsharesWalletWraper();
        try{
            log.info("转账接口调用开始: fromUserName:"+fromUserName);
            long startTime = System.currentTimeMillis();
            bww =  initWallet(bww ,OwnerInfoConstant.WALLET_ORIGIN_PWD,OwnerInfoConstant.BORDERLESS_BLOCK_SERVER,strMemo,fromUserName);
            signed_transaction signedTransaction = bww.transfer(fromUserName, toUserName,amountStr,assetSymbol,
                    memoInfo,   amountToFee, symbolToFee);
            if(null != signedTransaction){
                log.info("调用区块转账成功"+GsonUtil.GsonString(signedTransaction));
            }
            bww.close();
            long endTime = System.currentTimeMillis();

            log.info(" 用时: "+(endTime - startTime));
            log.info("查询用户区块信息结束: fromUserName "+fromUserName);
            return signedTransaction;

        }catch (Exception e){
            e.printStackTrace();
            bww.close();
            log.error("调用区块转账失败:",e.getStackTrace());
            return null;
        }

    }

    /**
     * 调用区块卖出
     * @param blockSellBo
     * @param priKey
     * @param userBlockName
     * @return
     */
    @Async("bitshareWalletWraper")
    public signed_transaction sell (BlockSellBo blockSellBo,String priKey,String userBlockName){
        BitsharesWalletWraper bww = new BitsharesWalletWraper();
        try{
            log.info("市场卖出调用开始: userBlockName:"+userBlockName);
            long startTime = System.currentTimeMillis();
            bww =  initWallet(bww ,OwnerInfoConstant.WALLET_ORIGIN_PWD,OwnerInfoConstant.BORDERLESS_BLOCK_SERVER,priKey,userBlockName);

            signed_transaction signedTransaction = bww.sell(blockSellBo.getQuote(),blockSellBo.getBase(),blockSellBo.getAmountToSell(),
                   blockSellBo.getAmount(),blockSellBo.getAmount_to_fee(),blockSellBo.getSymbol_to_fee() ,blockSellBo.getIndex() );
            if(null != signedTransaction){
                log.info("调用区块卖出成功"+GsonUtil.GsonString(signedTransaction));
            }
            bww.close();
            long endTime = System.currentTimeMillis();

            log.info(" 用时: "+(endTime - startTime));
            log.info("查询用户区块信息结束: userBlockName "+userBlockName);
            return signedTransaction;

        }catch (Exception e){
            e.printStackTrace();
            bww.close();
            log.error("调用区块卖出失败:",e.getStackTrace());
            return null;
        }

    }

    /**
     *
     * @param priKey
     * @param userBlockName
     * @return
     */
    @Async("bitshareWalletWraper")
    public signed_transaction buy (BlockBuyBo blockBuyBo, String priKey, String userBlockName){
        BitsharesWalletWraper bww = new  BitsharesWalletWraper();
        try{
            log.info("市场买入调用开始: userBlockName:"+userBlockName);
            long startTime = System.currentTimeMillis();
            bww =  initWallet(bww ,OwnerInfoConstant.WALLET_ORIGIN_PWD,OwnerInfoConstant.BORDERLESS_BLOCK_SERVER,priKey,userBlockName);

            signed_transaction signedTransaction = bww.buy(blockBuyBo.getQuote(),blockBuyBo.getBase(),blockBuyBo.getAmountToSell(),
                   blockBuyBo.getAmount(), blockBuyBo.getAmount_to_fee(),blockBuyBo.getSymbol_to_fee() ,blockBuyBo.getIndex());
            if(null != signedTransaction){
                log.info("调用区块买入成功"+GsonUtil.GsonString(signedTransaction));
            }
            bww.close();
            long endTime = System.currentTimeMillis();

            log.info(" 用时: "+(endTime - startTime));
            log.info("市场买入调用结束: userBlockName "+userBlockName);
            return signedTransaction;
        }catch (Exception e ){
            e.printStackTrace();
            bww.close();
            log.error("调用区块买入失败:",e.getStackTrace());
            return null;
        }


    }

    public signed_transaction cancelOrder (String id, String priKey , String userBlockName  ){

        BitsharesWalletWraper bww = new  BitsharesWalletWraper();
        try{
            log.info("市场取消订单调用开始: userBlockName:"+userBlockName);
            long startTime = System.currentTimeMillis();
            bww =  initWallet(bww ,OwnerInfoConstant.WALLET_ORIGIN_PWD,OwnerInfoConstant.BORDERLESS_BLOCK_SERVER,priKey,userBlockName);
            object_id<limit_order_object> limit_order_id = object_id.create_from_string(id);
            signed_transaction signedTransaction = bww.cancel_order(limit_order_id);
            if(null != signedTransaction){
                log.info("调用区块取消交易成功"+GsonUtil.GsonString(signedTransaction));
            }
            bww.close();
            long endTime = System.currentTimeMillis();

            log.info(" 用时: "+(endTime - startTime));
            log.info("市场取消订单调用结束: userBlockName "+userBlockName);
            return signedTransaction;
        }catch (Exception e ){
            e.printStackTrace();
            bww.close();
            log.error("调用区块取消交易失败:",e.getStackTrace());
            return null;
        }
    }

    /**
     * 获取交易记录
     * @param accountObjectId 用户区块账户区块id
     * @param nLimit 查询条数
     * @param flag 起始点id号
     * @return
     */
    public List<HistoryResponseModel.DataBean> getTransferHistory(object_id<account_object> accountObjectId,
                                                                  int nLimit,String flag){
        BitsharesWalletWraper bww = new  BitsharesWalletWraper();
        List<HistoryResponseModel.DataBean> hisLs = new ArrayList<>();
        try{
            hisLs = bww.get_transfer_history_by_flag(accountObjectId,nLimit,flag);
            bww.close();
            return hisLs;
        }catch (Exception e){
            e.printStackTrace();
            bww.close();
            log.error("调用区块查询交易记录失败:",e.getStackTrace());
            return null;
        }

    }

    /**
     * 初始化钱包对象导入私key
     * @param bww
     * @param walletOriginPwd
     * @param serverUrl
     * @param priKey
     * @param userBlockName
     * @return
     */
    private BitsharesWalletWraper initWallet(BitsharesWalletWraper bww ,String walletOriginPwd,
                                             String serverUrl,String priKey,String userBlockName ){
        log.info("钱包导入私钥调用开始: userBlockName:"+userBlockName);
        long startTime = System.currentTimeMillis();
        /*构建钱包对象*/
        int m = bww.build_connect();
        if(m==0){
            /*设置钱包密码*/
            bww.set_passwrod(walletOriginPwd);
            /*加密chper_key*/
            /*解锁钱包文件*/
            bww.unlock(walletOriginPwd);
            /*根据账户名，取区块链账户对象信息*/
            account_object account = null;

            try {
                account = bww.get_account_object(userBlockName);
            } catch (NetworkStatusException e) {
                e.printStackTrace();
                log.error("未查询到区块中的用户信息"+ e.getMessage(),e.getStackTrace());
            }
            if (account != null) {
                int s = bww.import_key(userBlockName,
                        priKey);
                log.info("钱包导入私key 成功.");
            }

        }
        long endTime = System.currentTimeMillis();

        log.info(" 用时: "+(endTime - startTime));
        log.info("钱包导入私钥调用结束: userBlockName "+userBlockName);
        return bww;

    }


}
