package com.kiki.skill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 作用 声明当前类是一个配置类,相当于一个Spring的XML配置文件,与@Bean配
 * @Configuration标注在类上，相当于把该类作为spring的xml配置文件中的<beans>，作用为：配置spring容器(应用上下文)
 */
@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    // 队列名称
    public static final String MAOSHA_QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEAD_QUEUE = "head.queue";
    public static final String HEAD_EXCHANGE = "headExchange";
    /**
     * 1.Direct模式  exchange 交换机模式
     * 发送者，将消息往外面发送的时候，并不是直接投递到队列里面去，而是先发送到交换机上面，然后由交换机发送数据到queue上面去，
     * 做了依次路由。
     * @return
     */
    @Bean
    public Queue queue(){
        // 名称, 是否持久化
        return new Queue(MAOSHA_QUEUE,true);
    }

//    /**
//     * 2.topic模式  exchange 交换机模式
//     */
//    @Bean
//    public Queue queue1(){
//        // 名称, 是否持久化
//        return new Queue(TOPIC_QUEUE1,true);
//    }
//    @Bean
//    public Queue queue2(){
//        // 名称, 是否持久化
//        return new Queue(TOPIC_QUEUE2,true);
//    }
//
//    @Bean
//    public TopicExchange topicExchange(){
//        return new TopicExchange(TOPIC_EXCHANGE);
//    }
//    // 将消息队列绑定到交换机中
//    @Bean
//    public Binding binding1(){
//        return BindingBuilder.bind(queue1()).to(topicExchange()).with("topic.key1");
//    }
//
//    @Bean
//    public Binding binding2(){
//        //#匹配一个或多个
//        return BindingBuilder.bind(queue2()).to(topicExchange()).with("topic.#");
//    }
//
//    /**
//     * 3.广播模式 fanout exchange
//     */
//
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(FANOUT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding fanoutBinding1(){
//        return BindingBuilder.bind(queue1()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding fanoutBinding2(){
//        return BindingBuilder.bind(queue2()).to(fanoutExchange());
//    }
//
//    /**
//     * 4.heads模式
//     */
//    @Bean
//    public  Queue headQueue(){
//        return new Queue(HEAD_QUEUE,true);
//    }
//
//    @Bean
//    public HeadersExchange headersExchange(){
//        return new HeadersExchange(HEAD_EXCHANGE);
//    }
//
//    /**
//     * 在message中，会有一个head部分，必须满足key-value，然后符合了后，才能在queue中放东西
//     * @return
//     */
//    @Bean
//    public Binding headerBinding(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("header1","value1");
//        map.put("header2","value2");
//        return BindingBuilder.bind(headQueue()).to(headersExchange()).whereAll(map).match();
//    }
}
