package com.buy.provider.service.impl;

import com.buy.commons.SeckillStatus;
import com.buy.provider.multuthreadtask.MultiThreadCreateOrder;
import com.buy.provider.service.LimitTimeBuyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LimitTimeBuyOrderServiceImpl implements LimitTimeBuyOrderService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MultiThreadCreateOrder multiThreadCreateOrder;

    /**
     * 收到下单的请求处理
     * @param id 商品的id
     * @param time 秒杀商品的时间区间
     * @param username 用户名称
     * @return
     */
    @Override
    public boolean addOrder(long id, String time, String username) {
        /*
         首先判断当前用户是否已经下过单，如果已经下过单则不能再次下单，避免用户刷单
         在redis中使用key为UserQueueCount(Hash类型)记录当前用户的下单记录,每一个用户只能下单一次
         hash中的key为username，value为下单的次数
         使用increment(username, 1)方法，直接对hash的value执行自增1，返回自增后的value的值。如果：
         返回值为1：则说明未下过单，可以下单
         返回值为2：则说明已经下过单，不能再下单
         判断结果 > 1,就是重复排队,那么拒绝继续下单
         */
        Long userQueueCount = redisTemplate.boundHashOps("UserQueueCount").increment(username, 1);
        System.out.println("userQueueCount:"+userQueueCount);
        //判断已下过单
        if(userQueueCount >1){
            //已经抢过单了,拒绝
            return false;
        }
        /*
           判断未下过单，则执行：（先把订单信息存入redis）
           把下单的请求,封装成SeckillStatus对象放入redis的List类型数据中模拟队列的先进先出排队效果
           秒杀状态 1:排队中，2:秒杀等待支付,3:支付超时，4:秒杀失败,5:支付完成
           第二个参数: 创建订单的时间
           第三个参数: 下单的状态
           第四个参数: 商品id
           第五个参数: 秒杀的时间区间
         */
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);
        //把下单的信息放入redis的List队列中,从左边放入,右边取出(先进的先出来)
        redisTemplate.boundListOps("limitTimeBuyOrderQueue").leftPush(seckillStatus);
        //存储用户的下单的排队信息
        redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
        //多线程异步下单操作
        multiThreadCreateOrder.createOrder();

        return true;
    }
    /**
     * 查询用户抢单的状态:如果status=2 则表示抢单成功，等待支付
     */
    @Override
    public SeckillStatus queryStatus(String username) {
        SeckillStatus seckillStatus = (SeckillStatus)redisTemplate.boundHashOps("UserQueueStatus").get(username);
        return seckillStatus;
    }
}
