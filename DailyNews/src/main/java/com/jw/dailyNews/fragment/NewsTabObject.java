package com.jw.dailyNews.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jw.dailyNews.R;
import com.jw.dailyNews.activity.ArticleActivity;
import com.jw.dailyNews.activity.ImageActivity;
import com.jw.dailyNews.adapter.DefaultAdapter;
import com.jw.dailyNews.adapter.HeaderAndFooterAdapter;
import com.jw.dailyNews.adapter.NewsObjectAdapter;
import com.jw.dailyNews.adapter.TopNewsAdapter;
import com.jw.dailyNews.base.BaseFragment;
import com.jw.dailyNews.bean.NewsObject;
import com.jw.dailyNews.protocol.NewsObjectProtocol;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.CommonUtils;
import com.jw.dailyNews.wiget.LoadingPage;
import com.jw.dailyNews.wiget.MyRefreshLayout;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import Lib.ThreadManager;

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 15:53
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class NewsTabObject extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        MyRefreshLayout.PullToUpRefreshListener<NewsObject.News>,ViewPager.OnPageChangeListener
        , DefaultAdapter.OnItemClickListener<NewsObject.News>{
    private int count=0;
    private TextView topTitle;
    private ArrayList<NewsObject.News> newsList;
    private List<NewsObject.News> focus;
    private CirclePageIndicator indicator;
    private List<String> links = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> descs = new ArrayList<>();
    private HeaderAndFooterAdapter mAdapter;
    private MyRefreshLayout swipeRefreshLayout;
    private TopNewsAdapter topNewsAdapter;
    private int currentPosition=0;
    //下拉刷新状态
    private static final int STATE_PULL_TO_DOWM_REFRESH=0;
    //上拉加载更多状态
    private static final int STATE_PULL_TO_UP_REFRESH=1;
    //轮播图开始滚动状态
    private static final int STATE_IMAGE_CIRCLE=2;
    private ViewPager viewpager;


    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                //下拉刷新，数据更新完毕，界面开始更新
                case STATE_PULL_TO_DOWM_REFRESH:
/*                    topNewsAdapter = new TopNewsAdapter(getContext(),links, descs, urls);
                    viewpager.setAdapter(topNewsAdapter);*/
                    topNewsAdapter.notifyDataSetChanged();
                    indicator.setCurrentItem(currentPosition);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                //上拉加载更多，数据更新完毕，界面开始更新
                case STATE_PULL_TO_UP_REFRESH:
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setLoading(false);
                    break;
                //轮播图开始滚动
                case STATE_IMAGE_CIRCLE:
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentPosition++;
                            if(currentPosition==descs.size()) {
                                currentPosition=0;
                            }
                            indicator.setCurrentItem(currentPosition);
                            //自循环
                            Message message=Message.obtain();
                            message.what=STATE_IMAGE_CIRCLE;
                            mHandler.sendMessage(message);
                        }
                    },4000);
                    break;
            }
            return false;
        }
    });

    @Override
    public View createSuccessView() {
        View view = View.inflate(getActivity(), R.layout.refresh_layout, null);
        //自己封装的带有下拉刷新和下拉加载更多功能的SwipeRefreshLayout
        swipeRefreshLayout = (MyRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        RecyclerView recycleView= (RecyclerView) view.findViewById(R.id.recycle_view);
        NewsObjectAdapter recycleAdapter = new NewsObjectAdapter(getActivity(), newsList);
        //RecycleAdapter的装饰类，使recycleView能够添加多个headView和footView
        mAdapter = new HeaderAndFooterAdapter(recycleAdapter);
        mAdapter.addHeaderView(initHeaderView());
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(mAdapter);
        recycleAdapter.setOnItemClickListener(this);
        //给swipeRefreshLayout设置下拉刷新progressBar颜色效果
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,Color.YELLOW, Color.RED);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setPullToUpRefreshListener(this);
        return view;
    }

    /**
     * 请求服务器 获取服务器的数据
     */
    protected LoadingPage.LoadResult load() {
        NewsObjectProtocol protocol=new NewsObjectProtocol(null,
                CommonUtils.createRecommondUrl(count),getContext(),"news");
        if(protocol.load()!=null){
            newsList = protocol.getNewsList();
            focus = protocol.getFocus();
        }
        return checkData(newsList); // 检查数据 有三种结果  成功, 错误,空
    }


    private View initHeaderView(){
        View headerView = View.inflate(getActivity(),R.layout.top_news,null);
        viewpager = (ViewPager) headerView.findViewById(R.id.nt_viewpager);
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.cp_indicator);
        topTitle = (TextView) headerView.findViewById(R.id.news_top_title);

        for(NewsObject.News news:focus){
            if(news.getImgsrc3gtype()==2){
                links.add(news.getPicInfo().get(0).getUrl());
                descs.add(news.getTitle());
                urls.add(news.getLink());
            }
        }
        topNewsAdapter = new TopNewsAdapter(getContext(),links, descs, urls);
        viewpager.setAdapter(topNewsAdapter);
        viewpager.addOnPageChangeListener(this);
        indicator.setViewPager(viewpager);
        indicator.setCurrentItem(currentPosition);
        topTitle.setText(descs.get(currentPosition));
        Message message=Message.obtain();
        message.what=STATE_IMAGE_CIRCLE;
        mHandler.sendMessage(message);
        return headerView;
    }

    @Override
    public void onItemClick(View v, int position, NewsObject.News news) {
        String url=news.getLink();
        Log.v("ObjectUrl",url);
        //跳转到图片信息页面
        if(url.contains("photoview")){
            Intent intent=new Intent(getActivity(),ImageActivity.class);
            intent.putExtra("docurl",url);
            getActivity().startActivity(intent);
        }//跳转到文章页面
        else if(url.contains("news")||url.contains("article")){
            Intent intent=new Intent(getActivity(),ArticleActivity.class);
            intent.putExtra("docurl",url);
            getActivity().startActivity(intent);
        }
    }

    /**
     * 下拉刷新监听
     */
    @Override
    public void onRefresh() {
        ThreadManager.getInstance().createLongPool(3,3
                ,2l).execute(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=count;i++)
                    CacheUtils.removeKey(CommonUtils.createRecommondUrl(count));
                NewsObjectProtocol protocol=new NewsObjectProtocol(null,
                        CommonUtils.createRecommondUrl(count=0),getContext(),"news");
                //清空所有list中的数据然后加载刷新读到的数据
                if(protocol.load()!=null){
                    newsList.clear();
                    newsList.addAll(protocol.getNewsList());
                    focus.clear();
                    focus.addAll(protocol.getFocus());
                }
                links.clear();descs.clear();urls.clear();
                for(NewsObject.News news:focus)
                    if(news.getImgsrc3gtype()==2){
                        links.add(news.getPicInfo().get(0).getUrl());
                        descs.add(news.getTitle());
                        urls.add(news.getLink());
                    }
                Message message=Message.obtain();
                message.what=STATE_PULL_TO_DOWM_REFRESH;
                mHandler.sendMessage(message);
                currentPosition=-1;
            }
        });
    }

    /**
     * 此处在子线程运行，如果请求的数据有更多，则跳转到hasMore，hasMore在主线程运行
     */
    @Override
    public List onLoad() {
        NewsObjectProtocol protocol=new NewsObjectProtocol(null,
                CommonUtils.createRecommondUrl(++count),getContext(),"news");
        List<NewsObject.News> newData = protocol.load().getList();
        return newData;
    }

    /**
     * 此处在主线程运行
     * @param newData
     */
    @Override
    public void hasMore(List newData) {
        newsList.addAll(newData);
        Message message = Message.obtain();
        message.what = STATE_PULL_TO_UP_REFRESH;
        mHandler.sendMessage(message);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //更新当前轮播图所在item对应的文字说明
    @Override
    public void onPageSelected(int position) {
        topTitle.setText(descs.get(position));
        currentPosition=position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
