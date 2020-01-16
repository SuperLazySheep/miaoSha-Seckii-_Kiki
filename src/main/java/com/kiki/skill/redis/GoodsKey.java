package com.kiki.skill.redis;

public class GoodsKey extends BaseKey {

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    // 有效期 60s , 前缀 gl
   public static  GoodsKey getGoodsList = new GoodsKey(60,"gl");
   public static GoodsKey getGetGoodsDetail = new GoodsKey(60,"gd");
    // 秒杀商品的数量
   public static GoodsKey getMiaoShaGoodsStock = new GoodsKey(0,"gs`");
}
