package com.buy.provider.timer;

import com.buy.commons.DateUtil;
import com.buy.mapper.TbItemLimitTimeBuyMapper;
import com.buy.pojo.TbItemLimitTimeBuy;
import com.buy.pojo.TbItemLimitTimeBuyExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 把从当前时间的起始时间点开始的参与限时降价的商品定时从数据库中查询出来存入redis缓存中
 */
@Component
public class TimerTask {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbItemLimitTimeBuyMapper itemLimitTimeBuyMapper;
    /**
     * 定时时间格式:[秒][分][小时][日][月][周][年]
     * @Scheduled(cron = "0 10-22 * * * ?")
     * 以上表示不指定年月日,每天的上午10点-晚上22点之间,每分钟的第0秒触发调用定时任务
     * 设置定时调用getGoodsToRedis方法
     */
    @Scheduled(cron = "0 * * * * ?")
    public void getGoodsToRedis(){
        System.out.println("定时执行查询并存入redis缓存");
        /**
         * 需求:根据当前所在的时间段开始的时间段,查询数据库中满足秒杀条件的商品
         */
        //1.获取从当前时间段开始的所有的时间段
        List<Date> dateMenus = DateUtil.getDateMenus();
        System.out.println(dateMenus.get(0));
        //2.根据每个时间段从数据库查当前时间段内的限时降价商品(startTime-->startTime+2)
        for(Date startTime:dateMenus){
            TbItemLimitTimeBuyExample exp = new TbItemLimitTimeBuyExample();
            TbItemLimitTimeBuyExample.Criteria criteria = exp.createCriteria();

            //指定查询的条件（以下条件必须同时满足）
            //1.商品必须审核通过的:即status = 1
            criteria.andStatusEqualTo("1");
            //2.库存>0：即stock_count>0
            criteria.andStockCountGreaterThan(0);
            //3.限时的开始时间 >= 当前时间段起始时间点
            criteria.andStartTimeEqualTo(startTime);
            //4.秒杀结束时间 <= 当前时间段结束时间点startTime+2
            Date endTime = DateUtil.addDateHour(startTime, 2);
            criteria.andEndTimeLessThanOrEqualTo(endTime);
            /*
            5.因为是定时重复的查询 ,需要先判断redis中是否已经存在该时间段的秒杀商品
            存入redis中的秒杀商品的key为limitTimeBuy_yyyyMMddHH的格式
             */
            String key = "limitTimeBuy_"+DateUtil.date2Str(startTime);
            //取到redis中所有的key
            Set keys = redisTemplate.boundHashOps(key).keys();
            if(keys != null && keys.size()>0){
                //如果key已经在redis中存在，则查询时需要避开已经在redis中存在的key
                criteria.andIdNotIn(new ArrayList<>(keys)); //Set转成List
            }
            //6.执行查询数据库
            List<TbItemLimitTimeBuy> itemLimitTimeBuys = itemLimitTimeBuyMapper.selectByExample(exp);
            System.out.println("正参与秒杀的商品: "+itemLimitTimeBuys);
            //7.将查出来的数据存入redis中
            for (TbItemLimitTimeBuy itemLimitTimeBuy:itemLimitTimeBuys) {
                redisTemplate.boundHashOps(key).put(itemLimitTimeBuy.getId(), itemLimitTimeBuy);
            }
        }
        //如果当前时间已经超过了限时商品的结束时间则从redis中删除该限时商品的数据
        //1.获取此刻时间段的前一个时间段的起始时间点(dateMenus.get(0)永远是此刻时间的所在时间段的起始时间点)
        Date date = DateUtil.addDateHour(dateMenus.get(0), -2);
        String key = "limitTimeBuy_"+DateUtil.date2Str(date);
        //2.把上一个时间段的key从redis中删除
        redisTemplate.delete(key);
    }
}
