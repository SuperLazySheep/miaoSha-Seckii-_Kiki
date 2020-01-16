package com.kiki.skill.service;

import com.kiki.skill.domain.OrderInfo;
import com.kiki.skill.domain.SkillOrder;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.vo.GoodsVo;

public interface OrderService {
    SkillOrder getOrderByUseridAndGoodsid_cache(Long id, Long goodsId);

    OrderInfo createOrder(SkillUser user, GoodsVo goods);

    OrderInfo getOrderById(long orderId);
}
