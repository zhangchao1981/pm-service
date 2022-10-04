package com.iscas.pm.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 */
public class DateUtil {
    public static String PATTERN_HH_MM_SS = "HH:mm:ss";
    public static String PATTERN_DEFAULT = "yyyy-MM-dd";
    public static String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate 较小的时间
     * @param endDate   较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static Integer daysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = sdf.parse(sdf.format(startDate));
            endDate = sdf.parse(sdf.format(endDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();

        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间的工作小时数
     * @param from  开始时间
     * @param to  结束时间
     * @return  小时数
     */
    public static long getWorkHours(Date from,Date to){
        List<Map<String, String>> onWorks = getOnWorkTimes();
        boolean exWeek = true;
        long mills = millsDiffExcluding(date2String(from,PATTERN_FULL), date2String(to,PATTERN_FULL), exWeek, onWorks);
        return mills/1000/60/60;
    }

    /**
     * 计算两个日期之间的相差毫秒数
     * "10:00:00", "10:01:00", "HH:mm:ss" -->6000
     * @return end-begin
     */
    public static long diffMilliseconds(String begin, String end, String pattern) {
        Date b = string2Date(begin, pattern);
        Date e = string2Date(end, pattern);
        if(b==null || e==null){
            return 0;
        }
        return e.getTime() - b.getTime();
    }

    /**字符串转日期*/
    public static Date string2Date(String dateStr, String... pattern) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            SimpleDateFormat formatter = null;
            if (pattern != null && pattern.length > 0) {
                formatter = new SimpleDateFormat(pattern[0].toString());
            } else {
                formatter = new SimpleDateFormat(PATTERN_DEFAULT);
            }
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**日期转字符串*/
    public static String date2String(Date date, String... pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = null;
        if (pattern != null && pattern.length > 0) {
            formatter = new SimpleDateFormat(pattern[0].toString());
        } else {
            formatter = new SimpleDateFormat(PATTERN_DEFAULT);
        }
        return formatter.format(date);
    }

    /**
     * 获取2个日期之间的日期列表
     * @param fromDateStr
     * @param toDateStr
     * @param excludeWeekend 是否排除周末
     */
    public static List<Date> getBetweenDates(String fromDateStr, String toDateStr, Boolean excludeWeekend) {
        List<Date> stamps = new ArrayList<>();
        Date fromDate = string2Date(fromDateStr);
        Date toDate = string2Date(toDateStr);
        while (fromDate.getTime() <= toDate.getTime()) {
            // 需要排除周末，则非周末的日期才添加到集合
            if (excludeWeekend) {
                if (!isWeekend(fromDate)) {
                    stamps.add(fromDate);
                }
            } else {
                stamps.add(fromDate);
            }
            fromDate = addDays(fromDate, 1);
        }
        if (stamps.size() == 0){
            return stamps;
        }

        int days = stamps.size();
        Date first = stamps.get(0),last = stamps.get(days-1);
        //from和days.get(0)是同一天，则集合中的第一个元素刷一下
        if(first.getTime() == string2Date(fromDateStr).getTime() ){
            stamps.set(0, string2Date(fromDateStr, PATTERN_FULL));
        }
        //to和days.get(last)是同一天，则集合中的最后一个元素刷一下
        if(last.getTime() == string2Date(toDateStr).getTime() ){
            stamps.set(days-1, string2Date(toDateStr, PATTERN_FULL));
        }

        return stamps;
    }

    /**
     * 某个时间加几天
     * @param date 某个时间基础
     * @param days 加的天份数（负数为减）
     * @return
     */
    public static Date addDays(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance(); // 得到日历
        calendar.setTime(date);// 把当前时间赋给日历
        calendar.add(Calendar.DATE, days); // 天加减
        return calendar.getTime(); // 加减后的时间
    }

    /**是否周末*/
    public static Boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int i = cal.get(Calendar.DAY_OF_WEEK);
        return i == 1 || i == 7;// 周日周六
    }

    /**获取某个时间之后的有效时长*/
    public static long getAfterHHMMMills(List<Map<String, String>> onWorks, String hhmm){
        long resMills = 0;
        long tsMills = 0;
        if(CollectionUtils.isEmpty(onWorks)){
            tsMills = diffMilliseconds(hhmm,"24:00:00",PATTERN_HH_MM_SS);
            System.out.println(String.format("[%s,24:00:00]有%s分钟",hhmm,tsMills/1000/60));
            return tsMills;
        }

        for (Map<String, String> work : onWorks) {
            tsMills = 0;
            // 07:20与08:30-12:00
            long millTemp = diffMilliseconds(hhmm, work.get("begin"), PATTERN_HH_MM_SS);
            long millTemp2 = diffMilliseconds(hhmm, work.get("end"), PATTERN_HH_MM_SS);
            //1提前2未到3之间
            if (millTemp > 0){
                System.out.print(String.format("%s < [%s，%s]", hhmm,work.get("begin"),work.get("end")));
                tsMills += diffMilliseconds(work.get("begin"), work.get("end"), PATTERN_HH_MM_SS);
            }else if(millTemp2 < 0){
                System.out.print(String.format("%s > [%s，%s]", hhmm,work.get("begin"),work.get("end")));
            }else{
                System.out.print(String.format("%s in [%s，%s]", hhmm,work.get("begin"),work.get("end")));
                tsMills += millTemp2;
            }
            resMills += tsMills;
            System.out.println(String.format("之后有%s分钟", tsMills/1000/60));
        }
        return resMills;
    }

    /**获取某个时间之前的有效时长*/
    public static long getBeforeHHMMMills(List<Map<String, String>> onWorks, String hhmm){
        long resMills = 0;
        long tsMills = 0;
        if(CollectionUtils.isEmpty(onWorks)){
            tsMills = diffMilliseconds("00:00:00", hhmm, PATTERN_HH_MM_SS);
            System.out.println(String.format("[00:00:00,%s]有%s分钟",hhmm,tsMills/1000/60));
            return tsMills;
        }

        for (Map<String, String> work : onWorks) {
            tsMills = 0;
            // 07:20与08:30-12:00
            long millTemp = diffMilliseconds(hhmm, work.get("begin"), PATTERN_HH_MM_SS);
            long millTemp2 = diffMilliseconds(hhmm, work.get("end"), PATTERN_HH_MM_SS);
            //1提前2未到3之间
            if (millTemp > 0){
                System.out.print(String.format("%s < [%s，%s]", hhmm,work.get("begin"),work.get("end")));
            }else if(millTemp2 < 0){
                System.out.print(String.format("%s > [%s，%s]", hhmm,work.get("begin"),work.get("end")));
                tsMills += diffMilliseconds(work.get("begin"), work.get("end"), PATTERN_HH_MM_SS);
            }else{
                System.out.print(String.format("%s in [%s，%s]", hhmm,work.get("begin"),work.get("end")));
                tsMills += (-millTemp);
            }
            resMills += tsMills;
            System.out.println(String.format("之前有%s分钟", tsMills/1000/60));
        }
        return resMills;
    }

    /**
     * 毫秒数转换为日时分秒，
     * @param ms
     * @return
     */
    public static String formatResult(long ms){
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }

    /**
     * 获取开始时间至结束时间期间的有效工作时长(精确到毫秒)
     *
     * @Title:minuteDiffExcluding
     * @Description: TODO
     * @date 2021年1月8日 下午9:20:32
     * @author yqwang
     * @param from
     * @param to
     * @param exWeek 是否排除周末
     * @param onWorks 上班时间可以定义 比如 上午 08:30 - 12:00  下午 13:00 - 17:30
     * @return
     */
    public static long millsDiffExcluding(String from, String to, Boolean exWeek, List<Map<String, String>> onWorks) {
        long resMills = 0;// 用于最终返回（毫秒数）
        // A.没有定义上班时间 且  不排除周末===>end - begin
        if(CollectionUtils.isEmpty(onWorks) && !exWeek){
            resMills = diffMilliseconds(from,to, PATTERN_FULL);
            return resMills;
        }

        // B.起止时间所包含的日期（有效日期） （第一天和最后一天带有时分秒）
        List<Date> dates = getBetweenDates(from, to, exWeek);
        int days = dates.size();
        if(days == 0){
            return 0;
        }
        System.out.println(String.format("一共%s天", days));
        System.out.println("范围内有效的时间列表：");
        for (Date date : dates) {
            System.out.println(date2String(date, PATTERN_FULL));
        }

        Date first = dates.get(0),last = dates.get(days-1);
        //如果一共就1天，那就3种情况，1：from和to是同一天，2：first和from一样，3：last和to一样
        // C.1天   一天时，时间需要处理一下,first和last的值  doing..............  比如要排除 周末时，1月8号10点 到 1月10号9点  or 1月10号9点到1月11号10点。。。
        if(days == 1){
            System.out.println("同一天！");
            if(string2Date(from).getTime() == string2Date(to).getTime()){//同一天
                first = string2Date(from, PATTERN_FULL);
                last = string2Date(to, PATTERN_FULL);
                long s = getAfterHHMMMills(onWorks, date2String(first,PATTERN_HH_MM_SS));
                System.out.println("<相减>");
                long e = getAfterHHMMMills(onWorks, date2String(last,PATTERN_HH_MM_SS));
                resMills+= (s-e);
            }else if(first.getTime() == string2Date(from, PATTERN_FULL).getTime()){//如：周五到周六
                last = null;
                resMills+= getAfterHHMMMills(onWorks, date2String(first,PATTERN_HH_MM_SS));
            }else{//如：周六到下周一
                first = null;
                resMills+= getBeforeHHMMMills(onWorks, date2String(last,PATTERN_HH_MM_SS));
            }
        }else{
            //D.跨天(包含了2天及以上)
            resMills+=getAfterHHMMMills(onWorks, date2String(first,PATTERN_HH_MM_SS));
            resMills+=getBeforeHHMMMills(onWorks, date2String(last,PATTERN_HH_MM_SS));
        }

        // E.2天以上:(days-2)*一天的有效值
        if (days > 2) {
            // 每个整天的有效毫秒数
            long allDayMills = 0;
            if (CollectionUtils.isEmpty(onWorks)) {
                allDayMills = 24*60*60*1000;
            } else {
                for (Map<String, String> work : onWorks) {
                    allDayMills += diffMilliseconds(work.get("begin"), work.get("end"), PATTERN_HH_MM_SS);
                }
            }
            resMills += (days-2)*allDayMills;
            System.out.println(String.format("中间%s天，每天%s分，共%s分", days-2,allDayMills/1000/60,(days-2)*allDayMills/1000/60));
        }

        return resMills;
    }

    /** 定义上班时间 */
    public static List<Map<String, String>> getOnWorkTimes() {
        List<Map<String, String>> onWorks = new ArrayList<Map<String, String>>(2);
        // 上班时间08:30-12:00,13:00-17:30
        Map<String, String> work1 = new HashMap<String, String>(2);
        Map<String, String> work2 = new HashMap<String, String>(2);
        work1.put("begin", "08:30:00");
        work1.put("end", "11:30:00");
        work2.put("begin", "13:00:00");
        work2.put("end", "17:30:00");
        onWorks.add(work1);
        onWorks.add(work2);
        return onWorks;
    }

}
