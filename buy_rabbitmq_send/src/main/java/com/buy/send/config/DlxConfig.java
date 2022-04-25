package com.buy.send.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置死信队列
 */
@Configuration
public class DlxConfig {
    //死信的队列名称
    public static final String DLX_TTL_QUEUE ="dlx_ttl_queue";
    //死信队列的路由key
    public static final String DLX_TTL_ROUT_KEY="dlx.ttl.key";//key的格式参考:java包的格式
    //创建死信队列
    @Bean
    public Queue dlxTtlQueue(){
        /*
                       队列名称          是否持久化          是否独享         是否自动删除
        public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete)
         */
        return new Queue(DLX_TTL_QUEUE,true,false,false);
    }

    /**
     * 绑定死信队列到交换机名称为(direct_exchange)的交换机和路由key("dlx.ttl.key")
     * @Qualifier这个注解是在spring中能从多个相同的类型bean对象中找到我们想要的
     * @param directExchange
     * @return
     */
    @Bean("dlxTtlBind")
    public Binding dlxTtlBind(@Autowired @Qualifier("directExchange") DirectExchange directExchange){
        return BindingBuilder.bind(dlxTtlQueue()).to(directExchange).with(DLX_TTL_ROUT_KEY);

    }
    /**
     * 点对点,完全匹配
     */
    public static final String DIRECT_EXCHANGE="direct_exchange";
    @Bean("directExchange")
    public DirectExchange directExchange(){
        return new DirectExchange(DIRECT_EXCHANGE,true,false);
    }
}
