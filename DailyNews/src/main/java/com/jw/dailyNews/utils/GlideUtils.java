package com.jw.dailyNews.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jw.dailyNews.BaseApplication;

import Lib.MyNews;

/**
 * Author: Administrator
 * Created on:  2017/8/16.
 * Description:
 */

public class GlideUtils {
    private static boolean isvImageDownloadOnlyWifi=CacheUtils.getCacheBoolean("isvImageDownloadOnlyWifi",true, MyNews.getInstance().getContext());
    private static boolean isWIFI=NetUtils.isNetworkAvailable(MyNews.getInstance().getContext()).equals("WIFI");
    public static void load(Context context, String url, ImageView view){
        if((isvImageDownloadOnlyWifi&&isWIFI)||!isvImageDownloadOnlyWifi)
            Glide.with(context).load(url).apply(BaseApplication.options).into(view);
    }
}
