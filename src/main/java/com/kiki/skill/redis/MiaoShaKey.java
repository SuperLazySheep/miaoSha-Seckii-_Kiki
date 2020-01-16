package com.kiki.skill.redis;

public class MiaoShaKey extends BaseKey {

    public MiaoShaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoShaKey getMiaoShaOver = new MiaoShaKey(0,"go");
    // 有效期 60s
    public static MiaoShaKey getMiaoShaPath = new MiaoShaKey(60,"gmsp");


}
