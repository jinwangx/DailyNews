package com.jw.dailyNews.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/15.
 */

public class DateUtils {

    /**
     * 传过来的时间与当前时间差值
     * @param format  时间格式为,如："yyyy-MM-dd HH:mm:ss"
     * @param pastTime 传入的时间
     * @return
     */
    public static String fromNow(String format,String pastTime){

        String time=null;

        DateFormat formater = new SimpleDateFormat(format);
        try
        {
            long d1 = System.currentTimeMillis();
            Date d2 = formater.parse(pastTime);
            long diff = d1 - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            if(hours<1&&days<1)
                time=minutes+"分钟前";
            else
                time=hours+"小时前";
            Log.v("time   ",time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 得到当前时间
     * @param format 格式，如"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getCurrentTime(String format){
        long time = System.currentTimeMillis();
        DateFormat formater = new SimpleDateFormat(format);
        return formater.format(time);
    }

    /**
     * 格式化时长
     * @param format 格式，如"HH:mm:ss"
     * @return
     */
    public static String getDuration(int durarion,String format){
        DateFormat formater = new SimpleDateFormat(format);
        return formater.format(durarion);
    }
}
