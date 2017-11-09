package com.jw.dailyNews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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