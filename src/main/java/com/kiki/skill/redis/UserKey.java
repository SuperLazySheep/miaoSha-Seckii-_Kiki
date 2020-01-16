package com.kiki.skill.redis;

public class UserKey extends BaseKey {


    public UserKey(String prefix) {
        super(prefix);
    }
    //id前缀
    public static UserKey idKey = new UserKey("id");
    //name前缀
    public static UserKey nameKEy = new UserKey("name");
}
