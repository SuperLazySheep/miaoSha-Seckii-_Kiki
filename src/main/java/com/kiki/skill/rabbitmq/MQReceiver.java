package com.kiki.skill.rabbitmq;

import com.kiki.skill.domain.SkillOrder;
import com.kiki.skill.domain.SkillUser;
import com.kiki.skill.redis.RedisService;
import com.kiki.skill.result.MsgCode;
import com.kiki.skill.result.ResultData;
import com.kiki.skill.service.GoodsService;
import com.kiki.skill.service.MiaoShaService;
import com.kiki.skill.service.OrderService;
import com.kiki.skill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQReceiver {

    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoShaService miaoShaService;
//
//   @RabbitListener(queues = MQConfig.QUEUE) //用来监听那个队列
//    public void receive(String msg){
//        log.info("receive  message: "+msg);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String msg){
//        log.info("receiveTopic1  message: "+msg);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String msg){
//        log.info("receiveTopic2  message: "+msg);
//    }
//
//    @RabbitListener(queues = MQConfig.HEAD_QUEUE)
//    public void receiveHeader(byte[] bytes){
//       log.info("receiveHead  message: "+ new String(bytes));
//    }

    @RabbitListener(queues = MQConfig.MAOSHA_QUEUE)
    public  void receiveMiaoShaQueue(String msg){
        log.info("miaosha  message " + msg);
        MiaoShaMessage message = redisService.stringToBean(msg, MiaoShaMessage.class);
        SkillUser user = message.getUser();
        Long goodId = message.getGoodId();
        // 接收到在进行判断数据库中库存是否够
        GoodsVo goodsVo= goodsService.getGoodsById(goodId);
        Integer stockCount = goodsVo.getStockCount();
       // System.out.println("库存------------------"+stockCount);
        if(stockCount <= 0){
            return;
        }
        // 判断是否秒杀过
        SkillOrder order = orderService.getOrderByUseridAndGoodsid_cache(user.getId(), goodId);
        if(order != null){
            return ;
        }
        //减库存， 下单
       // System.out.println("----下单");
        miaoShaService.miaoSha(user,goodsVo);
    }
}
