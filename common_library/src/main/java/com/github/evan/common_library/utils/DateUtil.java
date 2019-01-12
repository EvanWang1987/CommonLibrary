package com.github.evan.common_library.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.SimpleFormatter;

/**
 * Created by Evan on 2017/10/5.
 */
public class DateUtil {
    public static final String MM_dd_HH_mm = "MM-dd HH:mm";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_HH_mm_SS = "yyyy-MM-dd HH:mm:SS";
    public static final String HH_mm = "HH:mm";
    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String mm_ss = "mm:ss";
    public static final String ss = "ss";

    /**
     * 获取当前时间的Calendar
     * @return
     */
    public static Calendar getCalendarOfCurrentTimeMillis(){
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        return calendar;
    }

    /**
     * 当前时间转换日期字符串
     * @param template
     * @return
     */
    public static String currentTime2String(String template, Locale locale){
        return time2String(System.currentTimeMillis(), template, locale);
    }

    /**
     * 毫秒值转换日期字符串
     * @param time
     * @param template
     * @param locale
     * @return
     */
    public static String time2String(long time, String template, Locale locale){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat formatter = new SimpleDateFormat(template, locale);
        String value = formatter.format(calendar.getTime());
        return value;
    }

    /**
     * 计算时长, 计算一段时间差值的时分秒
     * @param time
     * @param template
     * @return
     */
    public static String time2String(long time, String template, String templateIfNoHour){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        int hour = calendar.get(Calendar.HOUR);
        SimpleDateFormat formatter = new SimpleDateFormat(hour <= 0 && !StringUtil.isEmptyString(templateIfNoHour, true) ? templateIfNoHour : template);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String value = formatter.format(calendar.getTime());
        return value;
    }

    public static String duration2String(long time, long nanoTime){
        return duration2String(time, nanoTime, null, null, null);
    }

    public static String duration2String(long time, long nanoTime, String hourTemplate, String minuteTemplate, String secondTemplate){
        if(time <= 0 && nanoTime > 0){
            return nanoTime + "纳秒";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millSecond = calendar.get(Calendar.MILLISECOND);
        String targetTemplate = null;
        if(hour > 0){
            targetTemplate = StringUtil.isEmptyString(hourTemplate, true) ? HH_mm_ss : hourTemplate;
        }else if(hour <= 0 && minute > 0){
            targetTemplate = StringUtil.isEmptyString(minuteTemplate, true) ? mm_ss : minuteTemplate;
        }else if(hour <= 0 && minute <= 0 && second > 0){
            targetTemplate = StringUtil.isEmptyString(secondTemplate, true) ? ss : secondTemplate;
        }else if(hour <= 0 && minute <=0 && second <=0 && millSecond > 0){
            return time + "毫秒";
        }else{
            return nanoTime + "纳秒";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(targetTemplate);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String value = formatter.format(calendar.getTime());
        return value;
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getCurrentYear(){
        Calendar calendar = getCalendarOfCurrentTimeMillis();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return
     */
    public static int getCurrentMonthOfYear(){
        Calendar calendar = getCalendarOfCurrentTimeMillis();
        return calendar.get(Calendar.MONTH) + 1;    //只有月份是从0开始
    }

    /**
     * 获取当前周数,年份内第几周
     * @return
     */
    public static int getCurrentWeekOfYear(){
        Calendar calendar = getCalendarOfCurrentTimeMillis();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前天数，年份内第几天
     * @return
     */
    public static int getCurrentDayOfYear(){
        Calendar calendar = getCalendarOfCurrentTimeMillis();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取当前周数，月份内第几周
     * @return
     */
    public static int getCurrentWeekOfMonth(){
        Calendar calendar = getCalendarOfCurrentTimeMillis();
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取当前天数，月份内第几天
     * @return
     */
    public static int getCurrentDayOfMonth(){
        Calendar calendar = getCalendarOfCurrentTimeMillis();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前天数，星期内第几天，星期几
     * @return
     */
    public static int getCurrentDayOfWeek(){
        Calendar calendar = getCalendarOfCurrentTimeMillis();
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;  //老外每周第一天是星期日
    }

}
