package com.buy.provider.service;

import com.buy.pojo.TbItemLimitTimeBuy;

import java.util.List;

public interface LimitTimeBuyService {
    /**
     * 获取指定时间段内的限时降价商品
     * @param time
     * @return
     */
    public List<TbItemLimitTimeBuy> getGoodListByTime(String time);

    /**
     * 获取指定时间段内的指定id的限时降价商品信息
     * @param time
     * @param id
     * @return
     */
    TbItemLimitTimeBuy selectByTimeAndId(String time, long id);
}
