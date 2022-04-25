package com.buy.provider.service.impl;

import com.buy.pojo.TbItemLimitTimeBuy;
import com.buy.provider.service.LimitTimeBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LimitTimeBuyServiceImpl implements LimitTimeBuyService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 从redis中获取指定时间段内的限时降价商品
     */
    @Override
    public List<TbItemLimitTimeBuy> getGoodListByTime(String time) {
        String key = "limitTimeBuy_"+time; // seckillGoods_2021122520
        List<TbItemLimitTimeBuy> values = (List<TbItemLimitTimeBuy>)redisTemplate.boundHashOps(key).values();
        return values;
    }
    /**
     * 从redis中获取指定时间段内指定id的限时降价商品信息
     */
    @Override
    public TbItemLimitTimeBuy selectByTimeAndId(String time, long id) {
        String key = "limitTimeBuy_"+time; // seckillGoods_2021122520
        TbItemLimitTimeBuy itemLimitTimeBuy = (TbItemLimitTimeBuy)redisTemplate.boundHashOps(key).get(id);
        return itemLimitTimeBuy;
    }
}
