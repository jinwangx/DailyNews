package com.jw.dailyNews;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Process;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mob.MobSDK;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
    private List<Activity> activitys = new LinkedList<>();
    private List<Service> services = new LinkedList<>();
    private static OkHttpClient client;
    private static Application application;
    private static int mainTid;
    public static RequestOptions options=new RequestOptions().centerCrop()
            .priority(Priority.HIGH)
            .placeholder(R.drawable.ic_default_news)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    public void onCreate() {
        super.onCreate();
        this.client = ProgressManager.getInstance().with(new OkHttpClient.Builder()).build();
        mainTid = android.os.Process.myTid();
        MyNews.getInstance().init(this);
        MobSDK.init(this,"1fb3a5b2acfd0","75bcfbefe27ffb97b280f550d3a1fe68");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Glide.with(this);
    }
    /**
     * @description OkHttpClient推荐一个应用就用一个对象，通过此方法可以拿到client对象
     * @version
     */



    public void addActivity(Activity activity) {
        activitys.add(activity);
    }

    public void removeActivity(Activity activity) {
        activitys.remove(activity);
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void removeService(Service service) {
        services.remove(service);
    }

    public void closeApplication() {
        closeActivitys();
        closeServices();
        Process.killProcess(mainTid);
    }

    private void closeActivitys() {
        ListIterator<Activity> iterator = activitys.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    private void closeServices() {
        ListIterator<Service> iterator = services.listIterator();
        while (iterator.hasNext()) {
            Service service = iterator.next();
            if (service != null) {
                stopService(new Intent(this, service.getClass()));
            }
        }
    }

    public static Application getApplication(){
        return application;
    }

    public static int getMainTid() {
        return mainTid;
    }
    public static OkHttpClient getOkHttpClient(){
        return client;
    }

}
