package com.jw.dailyNews.utils;

import android.support.v4.app.Fragment;

import com.jw.dailyNews.fragment.NewsTabNormal;
import com.jw.dailyNews.fragment.NewsTabObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Lib.NewsURL;

/**
 * 创建时间：2017/6/20
 * 更新时间：2017/11/11 0011 上午 12:30
 * 作者：Mr.jin
 * 描述：首页新闻页面各板块fragment工厂
 */
public class FragmentFactory {
    private  Map<Integer,Fragment> mFragments=new HashMap<Integer,Fragment>();
    private  List<String> urls=null;
    private static FragmentFactory instance;
    private  String[] titles;

    public static FragmentFactory getInstance(){
        synchronized (FragmentFactory.class) {
            if (instance == null)
                instance=new FragmentFactory();
            return instance;
        }
    }

    private FragmentFactory(){
        if(urls==null) {
            urls=new ArrayList<>();
            urls.add(CommonUtils.createRecommondUrl(0));
            urls.add(NewsURL.URL_HTTP_GUONEI);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_SHEHUI);
            urls.add(NewsURL.URL_HTTP_HISTORY);
            urls.add(NewsURL.URL_HTTP_ENTERTAINMENT);
            urls.add(NewsURL.URL_HTTP_ECONOMIC);

            urls.add(NewsURL.URL_HTTP_EDU);
            urls.add(NewsURL.URL_HTTP_GAME);
            urls.add(NewsURL.URL_HTTP_WAR);
            urls.add(NewsURL.URL_HTTP_SPORTS);
            urls.add(NewsURL.URL_HTTP_OWN);


            titles = new String[]{"推荐","国内", "国际", "社会", "历史","娱乐","财经","教育","游戏","军事","体育","独家"};
        }
    }

    public Fragment createFragment(final int position){
        Fragment fragment=null;
        fragment=mFragments.get(position);
        if(fragment==null) {
            if (position == 0) {
                fragment = new NewsTabObject();
            }
            else {
                fragment= NewsTabNormal.newInstance(urls.get(position));

            }
            if(fragment!=null)
                mFragments.put(position,fragment);
        }
        return fragment;
    }

    public String[] getTitles(){
        return titles;
    }
}
