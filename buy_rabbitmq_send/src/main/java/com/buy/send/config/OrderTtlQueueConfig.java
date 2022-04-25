package com.buy.send.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置过期队列
 */
@Configuration
public class OrderTtlQueueConfig {
    //过期队列的名称
    public static final  String TTL_ORDER_QUEUE="ttl_order_queue";

    public static final  String TTL_ROUT_KEY="#.ttl"; //#表示匹配所有以.ttl为后缀的路由键

    //配置队列
    @Bean("ttlOrderQueue")
    public Queue ttlOrderQueue(){
        /**
         * public Queue(String name, boolean durable, boolean exclusive,
         * boolean autoDelete, @Nullable Map<String, Object> arguments)
         * 第一个参数name : 队列的名称
         * 第二个参数durable  :是否持久化
         * 第三个参数exclusive  :是否独享
         * 第四个参数autoDelete :是否自动删除
         * 第五个参数arguments :Map集合,封装配置参数:
         *  x-dead-letter-exchange: 指定转发到死信队列的交换器名称
         *  x-dead-letter-routing-key: 指定死信交换器的路由key
         *  x-message-ttl: 过期时间的设置
         */
        Map<String,Object>  arguments= new HashMap<>();
        //全局设置：所有进入这个队列中的消息,统一设置过期时间为10秒钟
        arguments.put("x-message-ttl",10000);//单位是毫秒: 10秒

        //过期后转到死信队列
            //a.指定过期消息发送到的交换机名称为DlxConfig.DIRECT_EXCHANGE的死信交换器上
        arguments.put(DlxConstant.DEAD_LETTER_EXCHANGE,DlxConfig.DIRECT_EXCHANGE);
            //b.过期后发到--死信路由key(DlxConfig.DLX_TTL_ROUT_KEY)
        arguments.put(DlxConstant.DEAD_LETTER_QUEUE_KEY, DlxConfig.DLX_TTL_ROUT_KEY);

        Queue queue = new Queue(TTL_ORDER_QUEUE, true, false, false, arguments);
        return queue;
    }


    /**
     * 把该过期队列绑定到topic_exchange名称的topic交换机上架，通过路由键"#.ttl"
     * @param topicExchange
     * @return
     */
    @Bean("ttlOrderBind")
    public Binding ttlOrderBind(@Autowired @Qualifier("topicExchange") TopicExchange topicExchange){
        return BindingBuilder.bind(ttlOrderQueue()).to(topicExchange).with(TTL_ROUT_KEY);
    }

    /**
     * 点对点, 规则匹配
     */
    public static final String TOPIC_EXCHANGE="topic_exchange";
    @Bean("topicExchange")
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE,true,false);
    }
}
