package com.kiki.skill.redis;

/**
 * 暂时不设置过期时间
 */
public class OrderKey extends BaseKey {


    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getMiaoshaOrderByUidAndGid = new OrderKey(60*60,"ms_uidgid");
}
