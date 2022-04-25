package com.buy.web.controller;

import com.buy.commons.DateUtil;
import com.buy.commons.Result;
import com.buy.commons.SeckillStatus;
import com.buy.pojo.TbItemLimitTimeBuy;
import com.buy.pojo.TbUser;
import com.buy.web.service.LimitTimeBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
public class LimitTimeBuyController {
    @Autowired
    private LimitTimeBuyService limitTimeBuyService;

    /**
     * 获取限时活动的各个时间段（每2个小时为一个时时间段）
     * @return
     */
    @GetMapping("/seckill/goods/menus")
    public List<Date> getLimitTime(){
        return DateUtil.getDateMenus();
    }

    /**
     * 获取指定时间段内的限时降价商品
     */
    @GetMapping("/seckill/goods/list")
    public List<TbItemLimitTimeBuy> getGoodsList(String time){
        return (List<TbItemLimitTimeBuy>)limitTimeBuyService.getGoodListByTime(time);
    }

    /**
     * 获取指定时间段内的限时降价商品
     */
    @GetMapping("/seckill/goods/selOne")
    public TbItemLimitTimeBuy selectByTimeAndId(String time,long id){
        return limitTimeBuyService.selectByTimeAndId(time, id);
    }

    /**
     * 从redis中获取登录身份信息
     */
    @PostMapping("/getuser")
    public String getUser(HttpSession session){
        TbUser user= (TbUser)session.getAttribute("loginUser");
        System.out.println("user: "+user);
        return user.getUsername();
    }

    /**
     * 抢购的下单处理
     */
    @GetMapping("/seckill/order/add")
    public Result add(String time, long id, HttpSession session){
        //下单之前需要先判断是否是登录状态，是则可以直接下单，否则需要先登录
        TbUser user = (TbUser)session.getAttribute("loginUser");
        if(user != null){
            //处于登录状态
            System.out.println("username: "+user.getUsername());
            //下单处理
            boolean isAdd = limitTimeBuyService.addOrder(time, id, user.getUsername());
            if(isAdd){
                return new Result(0, "抢单成功");
            }else{
                return new Result(2, "100");//重复抢单
            }
        }else{
            //未登录
            return new Result(403, "还未登录,请先登录");
        }
    }

    /**
     * 查询用户抢单的状态:如果status=2 则表示抢单成功，等待支付
     */
    @GetMapping("/seckill/order/query")
    public Result queryStatus(HttpSession session){
        TbUser user = (TbUser) session.getAttribute("loginUser");
        String username = null;
        if(user != null){
            username = user.getUsername();
        }else{
            return new Result(403, "未登录,请先登录");
        }
        //调用service层查询
        SeckillStatus seckillStatus = limitTimeBuyService.queryStatus(username);
        if(seckillStatus != null){
            Result result = new Result(seckillStatus.getStatus(), "抢单状态");
            return result;
        }else {
            return new Result(404, "无信息");
        }
    }

}
