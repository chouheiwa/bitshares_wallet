package com.github.chouheiwa.wallet.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by dagou on 2017/9/21.
 */

public class NumberUtils {

    public static boolean compareFloatAndInt(float f, int i) {
        Float aFloat = Float.valueOf(f);
        int i1 = aFloat.compareTo((float) i);
        if (i1 == 0) {
            return true;
        }

        return false;

    }

    public static String formatDouble(double d) {
        String str = "";
        if (d >= 10000) {
            str = (d / 10000) + "m";
            return str;
        }

        if (d >= 1000) {
            str = (d / 1000) + "k";
            return str;
        }

        str = d + "";
        return str;
    }


    /**
     * 保留小数位
     */
    private static DecimalFormat decimalFormat0 = new DecimalFormat("#0");
    private static DecimalFormat decimalFormat1 = new DecimalFormat("#0.0");
    private static DecimalFormat decimalFormat2 = new DecimalFormat("#0.00");
    private static DecimalFormat decimalFormat5 = new DecimalFormat("#0.00000");
    private static DecimalFormat decimalFormat8 = new DecimalFormat("#0.00000000");


    public static String formatNumber(String s) {
        String str = "";
        //DecimalFormat decimalFormat = new DecimalFormat("#0.00000");
        str = decimalFormat5.format(Double.parseDouble(s));
        return str;
    }

    public static String formatNumber1(String s) {
        String str = "";
//        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        str = decimalFormat1.format(Double.parseDouble(s));
        return str;
    }

    public static String formatNumber0(String s) {
        String str = "";
//        DecimalFormat decimalFormat = new DecimalFormat("#0");
        str = decimalFormat0.format(Double.parseDouble(s));
        return str;
    }

    public static String formatNumber2(String s) {
        String str = "";
        // DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        if (TextUtils.isEmpty(s)) {
            str = "0.00";
        } else {
            str = decimalFormat2.format(Double.parseDouble(s));
        }

        return str;
    }

    public static String formatNumber8(String s) {
        String str = "";
        // DecimalFormat decimalFormat = new DecimalFormat("#0.00000000");
        if (TextUtils.isEmpty(s)) {
            str = "0.00";
        } else {
            str = decimalFormat8.format(Double.parseDouble(s));
        }

        return str;
    }

    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static String optString(String name, String fallback) {

        if (!TextUtils.isEmpty(name)) {
            return name;
        }
        return fallback;
    }
}
