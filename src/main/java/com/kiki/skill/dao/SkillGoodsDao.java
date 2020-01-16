package com.kiki.skill.dao;

import com.kiki.skill.domain.SkillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SkillGoodsDao {

    //stock_count > 0 的时候才去更新，数据库本身就会有锁，那么就不会在数据库中同时多个线程更新一条记录，使用数据库特性来保证超卖的问题
    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count>0")
    int reduceStock(SkillGoods skillGoods);

}
