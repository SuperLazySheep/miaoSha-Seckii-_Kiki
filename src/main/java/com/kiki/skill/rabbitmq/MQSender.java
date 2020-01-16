package com.kiki.skill.rabbitmq;

import com.kiki.skill.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    RedisService redisService;
    @Autowired
    AmqpTemplate amqpTemplate;


    public void send(Object obj){
        String msg = redisService.beanToString(obj);
        log.info("send  message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.MAOSHA_QUEUE,msg);
    }

//    // topic 模式
//    public void sendTopic(Object obj){
//        String msg = redisService.beanToString(obj);
//        log.info("send  message: "+msg);
//        // exchange routingKey message
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
//    }
//
//    // fanout 模式
//    //
//    public void sendFanout(Object object){
//        String msg = redisService.beanToString(object);
//        log.info("send  fanout message: "+msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
//        //amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg+"2");
//    }
//
//    //head 模式
//    public void sendHeader(Object object){
//        String msg = redisService.beanToString(object);
//        log.info("send  fanout message: "+msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1","value1");
//        properties.setHeader("header2","value2");
//        Message message = new Message(msg.getBytes(),properties);
//        amqpTemplate.convertAndSend(MQConfig.HEAD_EXCHANGE,null,message);
//    }
}
