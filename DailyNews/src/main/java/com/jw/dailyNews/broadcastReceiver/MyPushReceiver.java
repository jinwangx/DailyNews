package com.jw.dailyNews.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jw.dailyNews.activity.ArticleActivity;

import cn.jpush.android.api.JPushInterface;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Author: Administrator
 * Created on:  2017/8/7.
 * Description:
 */

public class MyPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            String extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
            JsonParser parser=new JsonParser();
            JsonObject root = parser.parse(extras).getAsJsonObject();
            String url = root.get("url").getAsString();
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, ArticleActivity.class);  //自定义打开的界面
            i.putExtra("docurl",url);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
