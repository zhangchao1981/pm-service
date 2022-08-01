package com.iscas.pm.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author： zhangchao
 * @Date： 2022/8/1
 * @Description： 日期计算工具类
 */
public class DateUtil {

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
}
