package com.buy.provider.controller;

import com.buy.api.LimitTimeBuyServiceApi;
import com.buy.commons.SeckillStatus;
import com.buy.pojo.TbItemLimitTimeBuy;
import com.buy.provider.service.LimitTimeBuyOrderService;
import com.buy.provider.service.LimitTimeBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LimitTimeBuyController implements LimitTimeBuyServiceApi {
    @Autowired
    private LimitTimeBuyService limitTimeBuyService;

    @Autowired
    private LimitTimeBuyOrderService limitTimeBuyOrderService;

    /**
     * 获取指定时间段内的限时降价商品
     */
    @Override
    public List<TbItemLimitTimeBuy> getGoodListByTime(String time) {
        List<TbItemLimitTimeBuy> goods = (List<TbItemLimitTimeBuy>)limitTimeBuyService.getGoodListByTime(time);
        return goods;
    }
    /**
     * 获取指定时间段内的限时降价商品
     */
    @Override
    public TbItemLimitTimeBuy selectByTimeAndId(String time, long id) {
        return limitTimeBuyService.selectByTimeAndId(time, id);
    }
    /**
     * 抢购的下单处理
     */
    @Override
    public boolean addOrder(String time, long id, String username) {
        return limitTimeBuyOrderService.addOrder(id, time, username);

    }
    /**
     * 查询用户抢单的状态:如果status=2 则表示抢单成功，等待支付
     */
    @Override
    public SeckillStatus queryStatus(String username) {
        SeckillStatus seckillStatus = limitTimeBuyOrderService.queryStatus(username);
        return seckillStatus;
    }
}
