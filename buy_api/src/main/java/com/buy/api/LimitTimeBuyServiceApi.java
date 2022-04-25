package com.buy.api;

import com.buy.commons.SeckillStatus;
import com.buy.pojo.TbItemLimitTimeBuy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface LimitTimeBuyServiceApi {
    //获取指定时间段内的限时降价商品
    @GetMapping("/seckill/goods/list")
    public List<TbItemLimitTimeBuy> getGoodListByTime(@RequestParam("time") String time);
    /**
     * 获取指定时间段内的限时降价商品
     */
    @GetMapping("/seckill/goods/selOne")
    public TbItemLimitTimeBuy selectByTimeAndId(@RequestParam("time") String time, @RequestParam("id") long id);
    /**
     * 抢购的下单处理
     */
    @GetMapping("/seckill/order/add")
    public boolean addOrder(@RequestParam("time") String time,@RequestParam("id") long id,@RequestParam("username") String username);
    /**
     * 查询用户抢单的状态:如果status=2 则表示抢单成功，等待支付
     */
    @GetMapping("/seckill/order/query")
    public SeckillStatus queryStatus(@RequestParam("username") String username);
}
