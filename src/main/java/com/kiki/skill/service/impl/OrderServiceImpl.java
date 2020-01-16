package com.kiki.skill.service.impl;

import com.kiki.skill.dao.OrderInfoDao;
import com.kiki.skill.dao.SkillOrderDao;
import com.kiki.skill.domain.OrderInfo;
import com.kiki.skill.domain.SkillOrder;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.redis.OrderKey;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.service.OrderService;
import com.kiki.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    SkillOrderDao skillOrderDao;
    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    RedisService redisService;

    /**
     * 查询 miaosha_order表的存库
     * @param id
     * @param goodsId
     * @return
     */
    @Override
    public SkillOrder getOrderByUseridAndGoodsid_cache(Long id, Long goodsId) {
        return redisService.get(OrderKey.getMiaoshaOrderByUidAndGid,""+id+""+goodsId,SkillOrder.class);
        //return skillOrderDao.getOrderByUseridAndGoodsid(id,goodsId);
    }

    /**
     * 生成订单 ，order_info和miaosha_order   , 事务
     * 为miaosha_order 创建索引 uid，goodsId ， 添加烧白直接callback
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    @Override
    public OrderInfo createOrder(SkillUser user, GoodsVo goods) {
        // 1.生成order_info
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setDeliveryAddrId(0L); // long类型 private Long
        orderInfo.setCreateDate(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        // 秒杀价格
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        // 订单状态  --  0 新建未支付  1-已支付  2-已发货  3-已收货
        orderInfo.setOrderStatus(0);
        // 用户id
        orderInfo.setUserId(user.getId());
        // 返回orderId
        // long id =
        orderInfoDao.insert(orderInfo);
        // 生成miaosha_order
        SkillOrder skillOrder = new SkillOrder();
        skillOrder.setGoodsId(goods.getId());
        // 将订单id传给秒杀订单里面的orderId
        skillOrder.setOrderId(orderInfo.getId());
        skillOrder.setUserId(user.getId());
        skillOrderDao.insertSkillOrder(skillOrder);
        // 成功之后 存入得到redis中
        redisService.set(OrderKey.getMiaoshaOrderByUidAndGid,""+user.getId()+""+goods.getId(),skillOrder);
        return orderInfo;
    }

    /**
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderInfo getOrderById(long orderId) {
        return orderInfoDao.getOrderById(orderId);
    }
}
















