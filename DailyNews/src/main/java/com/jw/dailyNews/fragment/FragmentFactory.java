package com.jw.dailyNews.fragment;

import android.support.v4.app.Fragment;

import com.jw.dailyNews.utils.CommonUtils;

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

            //由于只作为学习用，暂时不提供更多链接，普通新闻重复第二个新闻板块
            urls.add(CommonUtils.createRecommondUrl(0));
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);

            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);
            urls.add(NewsURL.URL_HTTP_WORLD);


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
