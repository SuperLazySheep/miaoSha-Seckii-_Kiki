package com.kiki.skill.redis;

public abstract class BaseKey implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    public BaseKey( String prefix){
        // 0代表永远不过期
        this.expireSeconds = 0;
        this.prefix = prefix;
    }

    public BaseKey(int expireSeconds, String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds(){
        return expireSeconds;
    }

    //前缀为 类名：+ prefix
    @Override
    public String getPrefix(){
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
