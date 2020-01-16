package com.kiki.skill.controller;

import com.kiki.skill.domain.OrderInfo;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.result.MsgCode;
import com.kiki.skill.result.ResultData;
import com.kiki.skill.service.GoodsService;
import com.kiki.skill.service.OrderService;
import com.kiki.skill.vo.GoodsVo;
import com.kiki.skill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public ResultData<OrderDetailVo> orderDetail(SkillUser user,
            @RequestParam("orderId") long orderId
      ){
        if(user == null){
            return ResultData.error(MsgCode.SESSION_EMPTY);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(orderInfo == null){
            return ResultData.error(MsgCode.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsService.getGoodsById(orderInfo.getGoodsId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setOrderInfo(orderInfo);
        return ResultData.success(detailVo);
    }
}
