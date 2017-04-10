package com.cpigeon.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/3.
 */

public class DateTool {

    //JDK中的日期格式（年-月-日）
    private final static String FORMAT_DATE = "yyyy-MM-dd";

    //JDK中的日期时间格式（年-月-日 时:分:秒）
    private final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    //字符串转换为日期
    public static Date strToDate(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    //字符串转换为日期时间
    public static Date strToDateTime(String strDateTime) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_DATETIME);
        Date dateTime = null;
        try {
            dateTime = dateTimeFormat.parse(strDateTime);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return dateTime;
    }

    //日期转换为字符串
    public static String dateToStr(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    //日期时间转换为字符串
    public static String dateTimeToStr(Date date) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_DATETIME);
        String strDateTime = dateTimeFormat.format(date);
        return strDateTime;
    }

    /**
     * 获取今天凌晨00:00的时间戳(毫秒)
     *
     * @return
     */
    public static long getTodayBeginTime() {
        Date currDate = new Date();
        return DateTool.strToDateTime(getDayBeginTimeStr(currDate)).getTime();
    }

    /**
     * 获取日期00:00的时间戳(毫秒)
     *
     * @return
     */
    public static long getDayBeginTime(Date date) {
        return DateTool.strToDateTime(getDayBeginTimeStr(date)).getTime();
    }


    /**
     * 日期转换为字符串 例如：2017-01-01 00:00:00
     *
     * @param date
     * @return
     */
    public static String getDayBeginTimeStr(Date date) {
        return String.format("%04d-%02d-%02d 00:00:00", date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    }

    /**
     * 获取友好时间 提示
     *
     * @param time
     * @return
     */
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param time
     * @return
     */
    public static String getTimeFormatText(String time) {
        Date date = strToDateTime(time);
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }
}
