package com.buy.provider.service;

import com.buy.commons.SeckillStatus;
public interface LimitTimeBuyOrderService {
    /**
     * 抢购的下单处理
     * @param id 商品的id
     * @param time 秒杀商品的时间区间
     * @param username 用户名称
     * @return
     */
    public boolean addOrder(long id,String time,String username);

    /**
     * 查询用户抢单的状态:如果status=2 则表示抢单成功，等待支付
     */
    public SeckillStatus queryStatus(String username);
}
