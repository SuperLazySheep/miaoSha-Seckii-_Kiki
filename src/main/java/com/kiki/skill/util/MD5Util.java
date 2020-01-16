package com.kiki.skill.util;


import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLEncoder;

/**
 * @author kiKI
 */
public class MD5Util {

    public static String md5(String inputPass){
        return DigestUtils.md5Hex(inputPass);
    }
    //固定颜值
    private static final String SALT = "1a2b3c4d";

    //第一次输入加密
    public static String inputPassToFromPass(String inputPass){
       String str = ""+SALT.charAt(0)+SALT.charAt(4)+inputPass+SALT.charAt(3)+SALT.charAt(5);
       return md5(str);
    }

    //第二次进数据库加密
    public static String fromPassToDBPass(String inputPass, String salt){
        String str = ""+salt.charAt(0)+salt.charAt(4)+inputPass+salt.charAt(3)+salt.charAt(5);
        return md5(str);
    }

    //加密
    public static String finalPass(String inputPass, String salt){
        String pass = inputPassToFromPass(inputPass);
        return fromPassToDBPass(pass,salt);
    }

//    public static void main(String[] args) {
//        System.out.println(inputPassToFromPass("123456"));
//        //28ebf2f555bc5f12b4b80967a29e3dec
//        System.out.println(fromPassToDBPass("123456","1a2b3c4d"));
//        System.out.println(finalPass("123456","1a2b3c4d"));
//        //585887ee6c09a9dc354121c4fe1ec2d3
//    }
}
