package com.jw.dailyNews.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 上午 12:35
 * 作者：Mr.jin
 * 描述：吐司工具类，单例且保证吐司在主线程运行
 */
public class ToastUtils {
	private static Toast mToast;
	public static void show(final Activity activity,final String content) {
		if(mToast==null)
			mToast=new Toast(activity);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.makeText(activity, content, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void show(final Activity activity, final int contentId) {
		if(mToast==null)
			mToast=new Toast(activity);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.makeText(activity, contentId, Toast.LENGTH_SHORT).show();
			}
		});
	}
}