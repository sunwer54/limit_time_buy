package com.buy.provider.multuthreadtask;

import com.alibaba.fastjson.JSON;
import com.buy.commons.IDUtils;
import com.buy.commons.SeckillStatus;
import com.buy.pojo.TbItemLimitTimeBuy;
import com.buy.pojo.TbOrderLimitTimeBuy;
import com.buy.send.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MultiThreadCreateOrder {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Sender sender;

    /**
     * @Async这是spring中用来定义异步任务的注解
     * @Async修饰类: 该类下所有的方法都是异步调用的
     * @Async修改方法: 这个方法是异步调用(在一个新的线程中执行)
     *
     */
    @Async
    public void createOrder(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //从redis的List队列中取下单请求排队的信息,准备下单
        SeckillStatus seckillStatus = (SeckillStatus)redisTemplate.boundListOps("limitTimeBuyOrderQueue").rightPop();
        //获取订单的参数：商品id
        Long id = seckillStatus.getGoodsId();
        String username = seckillStatus.getUsername();
        //获取订单的参数：商品所在的活动的时间段
        String time = seckillStatus.getTime();
        //根据时间段和商品id从redis中查询商品
        String key = "limitTimeBuy_"+time;
        TbItemLimitTimeBuy itemLimitTimeBuy = (TbItemLimitTimeBuy) redisTemplate.boundHashOps(key).get(id);
        if(itemLimitTimeBuy != null && itemLimitTimeBuy.getStockCount() >0){
            //创建订单
            TbOrderLimitTimeBuy orderLimitTimeBuy = new TbOrderLimitTimeBuy();
            orderLimitTimeBuy.setId(IDUtils.genItemId());//手动生成订单id
            orderLimitTimeBuy.setSeckillId(id);
            orderLimitTimeBuy.setMoney(itemLimitTimeBuy.getCostPrice());
            orderLimitTimeBuy.setUserId(username);
            orderLimitTimeBuy.setCreateTime(new Date());
            orderLimitTimeBuy.setStatus("0");//0 未支付状态,1 已支付
            //把订单存入redis缓存,用hash存储订单信息，key是"orderLimitTimeBuy"，hash的key是username，value是订单具体信息
            redisTemplate.boundHashOps("orderLimitTimeBuy").put(username,orderLimitTimeBuy);
            //订单创建后，执行库存的扣减
            itemLimitTimeBuy.setStockCount(itemLimitTimeBuy.getStockCount() -1);
            //库存扣减后，判断当redis中商品的库存=0时--->将数据同步更新到mysql中,清理redis中参与秒杀的商品
            if(itemLimitTimeBuy.getStockCount() == 0){
                //清理redis中参与秒杀的商品
                redisTemplate.boundHashOps(key).delete(id);
                //将数据同步更新到mysql中
            }else{
                //当库存还>0时，继续同步到redis的商品中
                redisTemplate.boundHashOps(key).put(id, itemLimitTimeBuy);
            }
            //更新redis中的下单状态(等待支付状态)
            seckillStatus.setOrderId(orderLimitTimeBuy.getId());
            seckillStatus.setMoney(orderLimitTimeBuy.getMoney().floatValue());
            seckillStatus.setStatus(2);// 2是等待支付
            redisTemplate.boundHashOps("UserQueueStatus").put(username, seckillStatus);
            //发送需要支付的消息
            String s = JSON.toJSONString(seckillStatus);
            sender.sendMessage(s);
            System.out.println("发送消息到过期队列成功");

        }
    }
}
