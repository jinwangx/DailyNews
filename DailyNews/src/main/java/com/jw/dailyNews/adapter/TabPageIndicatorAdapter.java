package com.jw.dailyNews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jw.dailyNews.fragment.FragmentFactory;

/**
 * 创建时间：2017/7/27
 * 更新时间：2017/11/11 0011 上午 12:24
 * 作者：Mr.jin
 * 描述：新闻首页页面TabPageIndicator适配器
 */
public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public TabPageIndicatorAdapter(FragmentManager fm) {
        super(fm);
        titles = FragmentFactory.getInstance().getTitles();
    }

    @Override
    public Fragment getItem(int position) {
        //新建一个Fragment来展示ViewPager item的内容，并传递参数
        return FragmentFactory.getInstance().createFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position % titles.length];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
