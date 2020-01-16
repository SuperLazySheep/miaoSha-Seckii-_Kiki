package com.kiki.skill.redis;

public class MiaoShaKey extends BaseKey {
    public MiaoShaKey(String prefix) {
        super(prefix);
    }

    public static MiaoShaKey getMiaoShaOver = new MiaoShaKey("go");
    public static MiaoShaKey getMiaoShaPath = new MiaoShaKey("gmsp");
}
