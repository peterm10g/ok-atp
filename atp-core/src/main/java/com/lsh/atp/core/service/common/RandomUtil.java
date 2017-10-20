package com.lsh.atp.core.service.common;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

public class RandomUtil {

    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String numChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String numberChar = "0123456789";

    public static String getRandomString(int length) { //length表示生成字符串的长度

        String base = allChar;

        return getRandomString(base,10);
    }

    public static String getRandomString(String base,int length) { //length表示生成字符串的长度

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getHoldNoStr(String channel,String sequence) { //length表示生成字符串的长度

        return channel + "_" + sequence + "_" + getRandomString(10);
    }

    public static String getRandom2String(int length) { //length表示生成字符串的长度
        return RandomStringUtils.randomNumeric(length);
    }


    public static void main(String[] args) {

        for(int i=0;i<100;i++){
            System.out.println(getHoldNoStr("1","343555"));
        }
    }


}