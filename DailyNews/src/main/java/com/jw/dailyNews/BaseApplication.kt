package com.jw.dailyNews

import Lib.MyNews
import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.Process
import cn.jpush.android.api.JPushInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mob.MobSDK
import me.jessyan.progressmanager.ProgressManager
import okhttp3.OkHttpClient
import java.util.*


/**
 * @date 创建时间：2017/6/16.
 * @author  Mr.jin
 * @description 自己的application，应用一开始就初始化,OkHttpClient,MyNews,ShareSDK
 * @version
 */

class BaseApplication : Application() {
    private val activities = LinkedList<Activity>()
    private val services = LinkedList<Service>()

    override fun onCreate() {
        super.onCreate()
        okHttpClient = ProgressManager.getInstance().with(OkHttpClient.Builder()).build()
        mainTid = android.os.Process.myTid()
        MyNews.get().init(this)
        MobSDK.init(this, "1fb3a5b2acfd0", "75bcfbefe27ffb97b280f550d3a1fe68")
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        Glide.with(this)
    }

    /**
     * @description OkHttpClient推荐一个应用就用一个对象，通过此方法可以拿到client对象
     * @version
     */


    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun addService(service: Service) {
        services.add(service)
    }

    fun removeService(service: Service) {
        services.remove(service)
    }

    fun closeApplication() {
        closeActivitys()
        closeServices()
        Process.killProcess(mainTid)
    }

    private fun closeActivitys() {
        val iterator = activities.listIterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            activity.finish()
        }
    }

    private fun closeServices() {
        val iterator = services.listIterator()
        while (iterator.hasNext()) {
            val service = iterator.next()
            if (service != null) {
                stopService(Intent(this, service.javaClass))
            }
        }
    }

    companion object {
        lateinit var okHttpClient: OkHttpClient
        var mainTid: Int = 0
        var options = RequestOptions().centerCrop()
                .priority(Priority.HIGH)
                .placeholder(R.drawable.ic_default_news)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

}
