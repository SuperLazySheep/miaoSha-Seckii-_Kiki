package com.kiki.skill.dao;

import com.kiki.skill.domain.SkillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SkillOrderDao {

    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    SkillOrder getOrderByUseridAndGoodsid(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into miaosha_order (user_id,goods_id,order_id) values (#{userId},#{goodsId},#{orderId})")
    void insertSkillOrder(SkillOrder skillOrder);
}
