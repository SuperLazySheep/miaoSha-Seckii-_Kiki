package com.kiki.skill.service.impl;

import com.kiki.skill.domain.OrderInfo;
import com.kiki.skill.domain.SkillOrder;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.redis.MiaoShaKey;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.service.OrderService;
import com.kiki.skill.service.SkillGoodsService;
import com.kiki.skill.service.MiaoShaService;
import com.kiki.skill.util.MD5Util;
import com.kiki.skill.util.UUIDUtil;
import com.kiki.skill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.provider.MD5;

import java.util.HashMap;
import java.util.Map;

@Service
public class MiaoShaServiceImpl implements MiaoShaService {

    @Autowired
    SkillGoodsService skillGoodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;


    /**
     * 对秒杀的操作，减 miaoshao_order的库存，添加 order_info与miaosha_order 两个表
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    @Override
    public OrderInfo miaoSha(SkillUser user, GoodsVo goods) {
        //1.减少库存,即更新库存
        boolean success = skillGoodsService.reduceStock(goods);
        // 下订单，其中有两个订单: order_info   miaosha_order
        if(success){
            OrderInfo orderInfo = orderService.createOrder(user, goods);
            return orderInfo;
        }else{
            // 减库存失败，做一个标记，代表商品已经秒杀完了
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 获取秒杀解果
     * 成功id
     * 失败返回 0 或 -1
     * 0 排队中， -1 代表库存不足
     * @param id
     * @param goodsId
     * @return
     */
    @Override
    public long getMiaoShaResult(Long userId, Long goodsId) {
        SkillOrder order = orderService.getOrderByUseridAndGoodsid_cache(userId, goodsId);
        // 成功
        if(order != null){
            return order.getOrderId();
        }else{
          // 失败
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else{
                return 0;
            }

        }
    }


    /**
     *  1-1
     */
    public void setGoodsOver(Long goodsId){
        redisService.set(MiaoShaKey.getMiaoShaOver,""+goodsId,true);
    }

    /**
     * 1-1
     */
    public boolean getGoodsOver(Long goodsId){
        return redisService.existsKey(MiaoShaKey.getMiaoShaOver,""+goodsId);
    }

    /**
     * 来获取秒杀接口的path，
     */
    @Override
    public String creatPath(SkillUser user, Long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoShaKey.getMiaoShaPath,""+user.getId()+"_"+goodsId,path);
        return path;
    }
}
