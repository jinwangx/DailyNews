package com.jw.dailyNews.utils;

import android.app.Activity;
import android.widget.Toast;

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