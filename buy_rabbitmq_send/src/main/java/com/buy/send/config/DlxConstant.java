package com.buy.send.config;

/**
 * 死信队列的标识符的类
 */
public class DlxConstant {
    /**
     * 死信交换器的标识符
     */
    public static final String DEAD_LETTER_EXCHANGE="x-dead-letter-exchange";
    /**
     * 死信路由key标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY="x-dead-letter-routing-key";
}
