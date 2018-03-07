package com.jw.dailyNews.wiget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.jw.dailyNews.R;
import com.jw.dailyNews.adapter.HeaderAndFooterAdapter;

import java.util.List;

/**
 * 创建时间：2017/7/31
 * 更新时间：2017/11/11 0011 上午 12:43
 * 作者：Mr.jin
 * 描述：具有下拉刷新和上拉加载更多功能的recycleView的refreshLayout
 */
public class MyRefreshLayout<Data> extends SwipeRefreshLayout {

    private RecyclerView mRecyclerView;
    private PullToUpRefreshListener mListener;
    private boolean isLoading;
    private View view;
    public static final int HAS_NO_MORE=0;  // 没有额外数据了
    public static final int LOAD_ERROR=1;// 加载失败
    public static final int HAS_MORE=2;//  有额外数据
    private float mDownY, mUpY;
    private Handler mHandler=new Handler();
    private RelativeLayout rlMoreLoading;
    private RelativeLayout rlMoreError;
    private RelativeLayout rlNoMore;


    public MyRefreshLayout(Context context) {
        this(context, null);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }

                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点
                mUpY = getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 获取ListView,设置ListView的布局位置
        if (mRecyclerView == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0)
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) instanceof RecyclerView) {
                    // 创建ListView对象
                    mRecyclerView = (RecyclerView) getChildAt(0);
                    // 设置ListView的滑动监听
                    mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            // 移动过程中判断时候能下拉加载更多
                            if (canLoadMore())
                                // 加载数据
                                loadData();
                        }
                    });
                }
        }
    }

    public int getCenterVisibleId(){
        int centerItemPosition=0;
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        //判断是当前layoutManager是否为LinearLayoutManager
        // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取最后一个可见view的位置
            int lastItemPosition = linearManager.findLastVisibleItemPosition();
            //获取第一个可见view的位置
            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            centerItemPosition=(lastItemPosition+firstItemPosition)/2;
        }
        return centerItemPosition;
    }

    /**
     * 判断是否满足加载更多条件:1.上拉状态 2.可见item是最后一个条目 3.不是正在加载状态
     * @return
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        boolean condition1 = (mDownY - mUpY) >= 40;

        // 2. 当前页面可见的item是最后一个条目
        boolean condition2 = false;
        int lastItemPosition = 0;
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            //判断是当前layoutManager是否为LinearLayoutManager
            // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                //获取最后一个可见view的位置
                lastItemPosition = linearManager.findLastVisibleItemPosition();
                //获取第一个可见view的位置
                //int firstItemPosition = linearManager.findFirstVisibleItemPosition();
            }
            //如果为true,则当前在最后一个item位置
            condition2 = lastItemPosition == (mRecyclerView.getAdapter().getItemCount() - 1);
        }

        // 3. 正在加载状态
        boolean condition3 = !isLoading;
        return condition1 && condition2 && condition3;
    }

    /**
     * 加载更多数据的逻辑
     */
    private void loadData() {
        if (mListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            //异步请求数据，并根据请求数据的分析,回到主线程处理相关事件
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    //请求数据
                    final List<Data> newData = mListener.onLoad();
                    //回到主线程中处理相关事件
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(newData==null){
                                refreshFootView(LOAD_ERROR);
                            }else if(newData.size()==0){
                                refreshFootView(HAS_NO_MORE);
                            }else{
                                refreshFootView(HAS_MORE);
                                mListener.hasMore(newData);
                            }
                            setLoading(false);
                        }
                    });
                }
            }.start();
        }

    }


    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        // 修改当前的状态
        isLoading = loading;
        if (isLoading) {
            // 显示布局
            if (view == null) {
                view = View.inflate(getContext(), R.layout.layout_load_more, null);
                rlMoreLoading = view.findViewById(R.id.rl_more_loading);
                rlMoreError = view.findViewById(R.id.rl_more_error);
                rlNoMore = view.findViewById(R.id.rl_no_more);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                ((HeaderAndFooterAdapter) (mRecyclerView.getAdapter())).addFootView(view);
            }
            refreshFootView(HAS_MORE);
        } else {
            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }
    }

    public void setPullToUpRefreshListener(PullToUpRefreshListener listener) {
        this.mListener = listener;
    }

    public interface PullToUpRefreshListener<Data> {
        List<Data> onLoad();
        void hasMore(List<Data> newData);
    }

    public void refreshFootView(Integer data) {
        switch (data){
            case LOAD_ERROR:
                rlMoreError.setVisibility(View.VISIBLE);
                rlMoreLoading.setVisibility(View.INVISIBLE);
                rlNoMore.setVisibility(View.INVISIBLE);
                break;
            case HAS_NO_MORE:
                rlMoreError.setVisibility(View.INVISIBLE);
                rlMoreLoading.setVisibility(View.INVISIBLE);
                rlNoMore.setVisibility(View.VISIBLE);
                break;
            case HAS_MORE:
                rlMoreError.setVisibility(View.INVISIBLE);
                rlMoreLoading.setVisibility(View.VISIBLE);
                rlNoMore.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
