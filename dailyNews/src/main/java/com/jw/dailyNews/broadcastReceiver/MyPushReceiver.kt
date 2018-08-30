package com.jw.dailyNews.broadcastReceiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.bumptech.glide.gifdecoder.GifHeaderParser.TAG
import com.google.gson.JsonParser
import com.jw.dailyNews.R
import com.jw.dailyNews.activity.ArticleActivity
import com.jw.dailyNews.activity.HomeActivity

/**
 * 创建时间：2017/8/7
 * 更新时间：2017/11/11 0011 上午 12:29
 * 作者：Mr.jin
 * 描述：极光推送广播接收者
 */
class MyPushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        Log.d(TAG, "onReceive - " + intent.action!!)

        if (JPushInterface.ACTION_REGISTRATION_ID == intent.action) {
            val regId = bundle!!.getString(JPushInterface.EXTRA_REGISTRATION_ID)
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId!!)
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action) {
            Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle!!.getString(JPushInterface.EXTRA_MESSAGE)!!)
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action) {
            Log.d(TAG, "收到了通知")
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action) {
            Log.d(TAG, "用户点击打开了通知")
            val extras = bundle!!.getString(JPushInterface.EXTRA_EXTRA)
            val parser = JsonParser()
            val root = parser.parse(extras).asJsonObject
            val url = root.get("url").asString
            //创建点跳转的Intent(这个跳转是跳转到通知详情页)
            val intent = Intent(context,ArticleActivity::class.java)
            intent.putExtra("docurl",url)
            //创建通知详情页的栈
            val stackBulider = TaskStackBuilder.create(context);
            //为其添加父栈 当从通知详情页回退时，将退到添加的父栈中
            stackBulider.addParentStack(HomeActivity::class.java)
            stackBulider.addNextIntent(intent)

            context.startActivity(intent)

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.action!!)
        }
    }
}
