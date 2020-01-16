package com.kiki.skill.redis;

public class SkillUserKey extends BaseKey {

    //2天的有效期
    private static final int TOKEN_EXPIRE = 3600*24*2;

    public SkillUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SkillUserKey token = new SkillUserKey(TOKEN_EXPIRE,"token");
    public static SkillUserKey getById = new SkillUserKey(TOKEN_EXPIRE,"id");
}
