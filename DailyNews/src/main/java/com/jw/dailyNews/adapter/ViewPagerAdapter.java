package com.jw.dailyNews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 上午 12:28
 * 作者：Mr.jin
 * 描述：图片新闻viewpager适配器
 */
public class ViewPagerAdapter extends PagerAdapter 
{
	private List<View> views;
	public ViewPagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		return this.views.size();
	}

	public Object instantiateItem(ViewGroup container, int position) {
		 container.addView(views.get(position));
		return views.get(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view==obj;
	}
	public void destroyItem(ViewGroup container, int position, Object object) {
		 container.removeView(views.get(position));
	}
}