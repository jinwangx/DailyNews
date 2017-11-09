package com.jw.dailyNews.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.ThemeUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.Unbinder;


public class BaseActivity extends AppCompatActivity {

	private List<Activity> activities=new LinkedList<Activity>();
	public Unbinder unbinder;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activities.add(this);
		initView();
	}

	public void killAll(){
		List<Activity> copy=new LinkedList<>(activities);
		for(Activity activity:copy){
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	protected void initView() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		ThemeUtils.changeStatusBar(this, CacheUtils.getCacheInt("indicatorColor", Color.RED,this));
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		activities.remove(this);
		if(unbinder!=null)
			unbinder.unbind();
	}

}
