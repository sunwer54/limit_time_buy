package com.buy.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description：获取时间段的工具类（每2个小时为一个时间段）
 */
public class DateUtil {

    /***
     * 从yyyy-MM-dd HH:mm格式转成yyyyMMddHH格式
     * @param dateStr
     * @return
     */
    public static String formatStr(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            //把字串格式的日期解析为Date类型
            Date date = simpleDateFormat.parse(dateStr);
            simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
            //把Date对象转化为指定日期格式，并返回该日期格式的字符串
            String format = simpleDateFormat.format(date);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 获取指定日期的凌晨的时间：00:00:00
     * @return
     */
    public static Date toDayStartHour(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);//传入日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);//传入日期的0时
        calendar.set(Calendar.MINUTE, 0);//传入日期的0分
        calendar.set(Calendar.SECOND, 0);//传入日期的0秒
        calendar.set(Calendar.MILLISECOND, 0);//传入日期的0毫秒
        Date start = calendar.getTime();//得到传入的日期的0时0分0秒0毫秒
        return start;
    }


    /***
     * 在当前时间基础上增加N分钟
     * @param date
     * @param minutes
     * @return
     */
    public static Date addDateMinutes(Date date,int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);//传入日期
        calendar.add(Calendar.MINUTE, minutes);// 24小时制
        date = calendar.getTime();
        return date;
    }

    /***
     * 在当前时间基础上递增N小时
     * @param hour
     * @return
     */
    public static Date addDateHour(Date date,int hour){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);// 24小时制
        date = calendar.getTime();
        return date;
    }

    /***
     * 获取时间菜单
     * @return
     */
    public static List<Date> getDateMenus(){
        //定义一个List<Date>集合，存储所有时间段
        //00:00:00,02:00:00,04:00:00,06:00:00
        //08:00:00,10:00:00,12:00:00,14:00:00
        //16:00:00,18:00:00,20:00:00,22:00:00
        List<Date> dates = new ArrayList<Date>();
        //循环12次
        Date date = toDayStartHour(new Date()); //当前日期的凌晨
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("date日期的凌晨:"+sdf.format(date));
        for (int i = 0; i <12 ; i++) {
            //每次递增2小时,将每次递增的时间存入到List<Date>集合中
            dates.add(addDateHour(date,i*2));
        }

        //判断当前时间属于哪个时间范围
        Date now = new Date();
        for (Date cdate : dates) {
            //开始时间<=当前时间<开始时间+2小时
            if(cdate.getTime()<=now.getTime() && now.getTime()<addDateHour(cdate,2).getTime()){
                //把当前时间设置为所在的时间段范围的起始的时间
                now = cdate;
                break;
            }
        }

        //当前需要显示的时间菜单
        List<Date> dateMenus = new ArrayList<Date>();
        //只得到当前时间段范围的起始时间段到之后的连续的共5个时间段
        for (int i = 0; i <5 ; i++) {
            dateMenus.add(addDateHour(now,i*2));
        }
        return dateMenus;
    }

    /***
     * 时间转成yyyyMMddHH
     * @param date
     * @return
     */
    public static String date2Str(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
        return simpleDateFormat.format(date);
    }
}
