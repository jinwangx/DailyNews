package com.jw.dailyNews.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jw.dailyNews.R;
import com.jw.dailyNews.activity.ArticleActivity;
import com.jw.dailyNews.activity.ImageActivity;
import com.jw.dailyNews.adapter.DefaultAdapter;
import com.jw.dailyNews.adapter.HeaderAndFooterAdapter;
import com.jw.dailyNews.adapter.NewsNormalAdapter;
import com.jw.dailyNews.base.BaseFragment;
import com.jw.dailyNews.domain.NewsNormal;
import com.jw.dailyNews.protocol.NewsNormalProtocol;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.CommonUtils;
import com.jw.dailyNews.wiget.LoadingPage;
import com.jw.dailyNews.wiget.MyRefreshLayout;

import java.util.List;

import Lib.ThreadManager;

/**
 * 创建时间：2017/7/18
 * 更新时间 2017/10/30 15:53
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class NewsTabNormal extends BaseFragment {
    RecyclerView recycleView;
    MyRefreshLayout swipeRefreshLayout;
    private List<NewsNormal> newsNormalList;
    private int count = 0;
    private String url;
    private HeaderAndFooterAdapter mAdapter;
    private static final int STATE_PULL_TO_DOWM_REFRESH=0;
    private static final int STATE_PULL_TO_UP_REFRESH=1;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case STATE_PULL_TO_DOWM_REFRESH:
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case STATE_PULL_TO_UP_REFRESH:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    public static NewsTabNormal newInstance(String url){
        NewsTabNormal newsTabNormal=new NewsTabNormal();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        newsTabNormal.setArguments(bundle);
        return newsTabNormal;
    }

    @Override
    public View createSuccessView() {
        View view = View.inflate(getActivity(), R.layout.refresh_layout, null);
        swipeRefreshLayout= (MyRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recycleView= (RecyclerView) view.findViewById(R.id.recycle_view);
        NewsNormalAdapter recycleAdapter = new NewsNormalAdapter(getActivity(), newsNormalList);
        recycleAdapter.setOnItemClickListener(new DefaultAdapter.OnItemClickListener<NewsNormal>() {
            @Override
            public void onItemClick(View v,int position,NewsNormal news) {
                String url;
                if(news.getSkipURL()!=null)
                   url=news.getSkipURL();
                else
                    url=news.getUrl();
                Log.v("urlllllll",url);
                if(url.contains("photoview")){
                    Intent intent=new Intent(getActivity(),ImageActivity.class);
                    intent.putExtra("docurl",url);
                    getActivity().startActivity(intent);
                }else if(url.contains("news")||url.contains("article")){
                    Intent intent=new Intent(getActivity(),ArticleActivity.class);
                    intent.putExtra("docurl",url);
                    getActivity().startActivity(intent);
                }else {
                    Intent intent=new Intent(getActivity(),ArticleActivity.class);
                    intent.putExtra("docurl",url);
                    getActivity().startActivity(intent);
                }
            }
        });
        mAdapter = new HeaderAndFooterAdapter(recycleAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,Color.YELLOW, Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (int i = 0; i <= count; i++)
                    CacheUtils.clearCache(CommonUtils.createNewsUrl(url, count), getContext());
                ThreadManager.getInstance().createLongPool(3, 3, 2l).execute(new Runnable() {
                    @Override
                    public void run() {
                        NewsNormalProtocol protocol = new NewsNormalProtocol(url, CommonUtils.createNewsUrl(url, count = 0), getContext(), "news");
                        newsNormalList.clear();
                        newsNormalList.addAll(protocol.load());
                        Message message=Message.obtain();
                        message.what=STATE_PULL_TO_DOWM_REFRESH;
                        mHandler.sendMessage(message);
                    }
                });
            }
        });
        swipeRefreshLayout.setPullToUpRefreshListener(new MyRefreshLayout.PullToUpRefreshListener<NewsNormal>() {
            @Override
            public List<NewsNormal> onLoad() {
                NewsNormalProtocol protocol = new NewsNormalProtocol(url, CommonUtils.createNewsUrl(url, ++count), getContext(), "news");
                List<NewsNormal> newData = protocol.load();
                return newData;
            }

            @Override
            public void hasMore(List<NewsNormal> newData) {
                newsNormalList.addAll(newData);
                Message message=Message.obtain();
                message.what=STATE_PULL_TO_DOWM_REFRESH;
                mHandler.sendMessage(message);
            }
        });
        return view;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        Bundle arguments = getArguments();
        url=arguments.getString("url");
        NewsNormalProtocol protocol = new NewsNormalProtocol(url, CommonUtils.createNewsUrl(url, 0), getContext(), "news");
        newsNormalList = protocol.load();
        return checkData(newsNormalList);
    }
}
