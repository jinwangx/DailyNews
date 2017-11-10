package com.jw.dailyNews.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jw.dailyNews.R;
import com.jw.dailyNews.adapter.HeaderAndFooterAdapter;
import com.jw.dailyNews.adapter.JokeAdapter;
import com.jw.dailyNews.base.BaseFragment;
import com.jw.dailyNews.bean.Joke;
import com.jw.dailyNews.protocol.JokeProtocol;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.wiget.DividerItemDecoration;
import com.jw.dailyNews.wiget.LoadingPage;
import com.jw.dailyNews.wiget.MyRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import Lib.NewsURL;
import Lib.ThreadManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:52
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class FragmentVideo extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        MyRefreshLayout.PullToUpRefreshListener{

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.swipe_refresh_layout)
    MyRefreshLayout mSwipeRefreshLayout;
    public static AppCompatActivity activity;
    private int count = 0;
    private List<Joke> jokeList=new ArrayList<>();
    private HeaderAndFooterAdapter mAdapter;
    private static final int STATE_PULL_TO_DOWM_REFRESH=0;
    private static final int STATE_PULL_TO_UP_REFRESH=1;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case STATE_PULL_TO_DOWM_REFRESH:
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case STATE_PULL_TO_UP_REFRESH:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });
    private JokeProtocol protocol;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View createSuccessView() {
        View view = View.inflate(getContext(), R.layout.refresh_layout, null);
        unbinder=ButterKnife.bind(this,view);
        JokeAdapter defaultAdapter = new JokeAdapter(getContext(), jokeList=protocol.getList(0));
        mAdapter=new HeaderAndFooterAdapter(defaultAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,Color.YELLOW, Color.RED);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setAdapter(mAdapter);
        //item之间分割线
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        //下拉刷新监听
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //上拉加载更多监听
        mSwipeRefreshLayout.setPullToUpRefreshListener(this);
        return view;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        activity= (AppCompatActivity) getActivity();
        protocol = new JokeProtocol(null, NewsURL.BAISIBUDEJIE_JOKE_HTTP, getContext(), "normal");
        List<Joke> jokeList = protocol.load();
        return checkData(jokeList);
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    /**
     * 下拉刷新监听
     */
    @Override
    public void onRefresh() {
        ThreadManager.getInstance().createLongPool(3, 3, 2l).execute(new Runnable() {
            @Override
            public void run() {
                CacheUtils.clearCache(NewsURL.BAISIBUDEJIE_JOKE_HTTP,getContext());
                protocol = new JokeProtocol(null, NewsURL.BAISIBUDEJIE_JOKE_HTTP,getContext(),"normal");
                protocol.load();
                jokeList.clear();
                jokeList.addAll(protocol.getList(0));
                Message message=Message.obtain();
                message.what=STATE_PULL_TO_DOWM_REFRESH;
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 此处在子线程运行，如果请求的数据有更多，则跳转到hasMore，hasMore在主线程运行
     */
    @Override
    public List onLoad() {
        count++;
        List<Joke> newData=null;
        if(count<protocol.lists.length)
            newData=protocol.getList(count);
        return newData;
    }

    /**
     * 此处在主线程运行
     * @param newData
     */
    @Override
    public void hasMore(List newData) {
        jokeList.addAll(newData);
        Message message=Message.obtain();
        message.what=STATE_PULL_TO_UP_REFRESH;
        mHandler.sendMessage(message);
    }
}
