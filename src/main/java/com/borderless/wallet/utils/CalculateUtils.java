package com.borderless.wallet.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/10/24.
 */

public class CalculateUtils {




    public static double add(double var1, double var2) {
        return new BigDecimal(Double.toString(var1)).add(new BigDecimal(Double.toString(var2))).doubleValue();

    }

    /**
     * 减法
     *
     * @param var1
     * @param var2
     * @return
     */

    public static double sub(double var1, double var2) {

        return new BigDecimal(Double.toString(var1)).subtract(new BigDecimal(Double.toString(var2))).doubleValue();
    }


    public static String sub(String var1, String var2) {

        return new BigDecimal(var1).subtract(new BigDecimal(var2)).toPlainString();
    }


    public static String mul(double var1, double var2) {

        return new BigDecimal(var1).multiply(new BigDecimal(var2)).toPlainString();
    }

    /**
     * 乘法上切
     *
     * @param var1
     * @param var2
     * @param scale 精度，到小数点后几位
     * @return
     */
    public static String mulScale(String var1, String var2,int scale) {
        return new BigDecimal(var1).multiply(new BigDecimal(var2)).setScale(scale, BigDecimal.ROUND_UP).toPlainString();
    }

    /**
     * 乘法四舍五入
     *
     * @param var1
     * @param var2
     * @param scale 精度，到小数点后几位
     * @return
     */
    public static String mulScaleHALF_DOWN(String var1, String var2,int scale) {
        return new BigDecimal(var1).multiply(new BigDecimal(var2)).setScale(scale, BigDecimal.ROUND_HALF_DOWN).toPlainString();
    }

    /**
     * 除法
     *
     * @param v1
     * @param v2
     * @param scale 精度，到小数点后几位 四舍五入
     * @return
     */

    public static String div(double v1, double v2, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or ");

        }
//        BigDecimal b1 = new BigDecimal(Double.toString(v1));
//        BigDecimal b2 = new BigDecimal(Double.toString(v2));
//
//        if (b2.doubleValue()  == 0 || TextUtils.isEmpty(b2.toString())){
//            return  "0";
//        }

        //return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toPlainString();
        if (v2==0){
            return "0";
        }
        return new BigDecimal(Double.toString(v1)).divide(new BigDecimal(Double.toString(v2)), scale, BigDecimal.ROUND_HALF_UP).toPlainString();


    }



    /**
     * 四舍五入
     * @param v
     * @param scale 精确位数
     * @return
     */
    public static double round(double v, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

//        BigDecimal b = new BigDecimal(Double.toString(v));
//
//        BigDecimal one = new BigDecimal("1");

//        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return new BigDecimal(Double.toString(v)).divide(new BigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }


    /**
     * 舍入后面有值 进位
     * @param v
     * @param scale 精确位数
     * @return
     */
    public static double roundUp(double v, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

//        BigDecimal b = new BigDecimal(Double.toString(v));
//
//        BigDecimal one = new BigDecimal("1");

//        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return new BigDecimal(Double.toString(v)).divide(new BigDecimal("1"), scale, BigDecimal.ROUND_UP).doubleValue();

    }


    /**
     * 舍去小数位
     * @param v
     * @return
     */
    public static int round(String v) {

        if (Double.parseDouble(v)==0){
            return 0;
        }
        if (TextUtils.isEmpty(v)){
            return 0;
        }
//
//        BigDecimal b = new BigDecimal(v);
//        BigDecimal one = b.setScale(0,BigDecimal.ROUND_DOWN);
//        return one.intValue();
        return new BigDecimal(v).setScale(0,BigDecimal.ROUND_DOWN).intValue();

    }

    public static String roundMarket(double v, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        }

        BigDecimal b = new BigDecimal(Double.toString(v));

        BigDecimal one = new BigDecimal("1");
        double dou = b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal bigDecimal = new BigDecimal(dou+"");
        String result = bigDecimal.toString();

        return result ;

    }

    public static double getDouble(String var){
       return new BigDecimal(var).doubleValue();
    }

}
