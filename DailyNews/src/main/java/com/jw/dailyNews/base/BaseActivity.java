package com.jw.dailyNews.base;

import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jw.dailyNews.BaseApplication;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建时间：
 * 更新时间 2017/11/2 16:31
 * 版本：
 * 作者：Mr.jin
 */

public abstract class BaseActivity extends AppCompatActivity{

	public Unbinder unbinder;
	protected List<BroadcastReceiver> receivers=new ArrayList<>();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		((BaseApplication)getApplication()).addActivity(this);
		bindView();
		unbinder= ButterKnife.bind(this);
		init();
		initView();
		loadData();
		initEvent();
	}

	/**
	 * 在初始化视图之前执行，如注册广播接收者，还有一些初始化操作
	 */
	protected void init(){

	}

	/**
	 * 绑定视图
	 */
	protected abstract void bindView();

	/**
	 * 初始化视图
	 */
	protected void initView(){
	}

	/**
	 * 初始化监听事件
	 */
	protected void initEvent(){

	}

	/**
	 * 读取数据
	 */
	protected void loadData(){

	}

	@Override
	protected void onResume() {
		super.onResume();
		ThemeUtils.changeStatusBar(this, CacheUtils.getCacheInt("indicatorColor", Color.RED));
		loadData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(unbinder!=null)
			unbinder.unbind();
		if(receivers.size()!=0){
			for(int i=0;i<receivers.size();i++){
				unregisterReceiver(receivers.get(i));
			}
		}
		((BaseApplication)getApplication()).removeActivity(this);
	}
}
