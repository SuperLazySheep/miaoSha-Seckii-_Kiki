package com.kiki.skill.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    /**
     * 验证手机号码
     */
    private static final Pattern PATTERN_PHONE = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = PATTERN_PHONE.matcher(mobile);
        return matcher.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(isMobile("123456789"));
//        System.out.println(isMobile("18091314778"));
//    }
}
