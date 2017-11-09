package com.jw.dailyNews;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mob.MobSDK;

import Lib.MyNews;
import cn.jpush.android.api.JPushInterface;
import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;


/**
 * @date 创建时间：2017/6/16.
 * @author  Mr.jin
 * @description 自己的application，应用一开始就初始化,OkHttpClient,MyNews,ShareSDK
 * @version
 */

public class BaseApplication extends Application {
    private static OkHttpClient client;
    private static int mainTid;
    public static RequestOptions options=new RequestOptions().centerCrop()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    public void onCreate() {
        super.onCreate();
        this.client = ProgressManager.getInstance().with(new OkHttpClient.Builder()).build();
        MyNews.getInstance().init(this);
        mainTid = android.os.Process.myTid();
        MobSDK.init(this,"1fb3a5b2acfd0","75bcfbefe27ffb97b280f550d3a1fe68");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Glide.with(this);
    }
    /**
     * @description OkHttpClient推荐一个应用就用一个对象，通过此方法可以拿到client对象
     * @version
     */
    public static OkHttpClient getOkHttpClient(){
        return client;
    }
    public static int getMainTid() {
        return mainTid;
    }
}
