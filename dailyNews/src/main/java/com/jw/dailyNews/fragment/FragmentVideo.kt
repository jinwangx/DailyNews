package com.jw.dailyNews.fragment

import Lib.NewsURL
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Message
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.jw.dailyNews.R
import com.jw.dailyNews.adapter.HeaderAndFooterAdapter
import com.jw.dailyNews.adapter.JokeAdapter
import com.jw.dailyNews.base.BaseFragment
import com.jw.dailyNews.bean.Joke
import com.jw.dailyNews.protocol.JokeProtocol
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.wiget.DividerItemDecoration
import com.jw.dailyNews.wiget.LoadingPage
import com.jw.dailyNews.wiget.MyRefreshLayout
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import java.util.*

/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:52
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

class FragmentVideo : BaseFragment(), SwipeRefreshLayout.OnRefreshListener
        , MyRefreshLayout.PullToUpRefreshListener<Joke> {

    lateinit var mRecycleView: RecyclerView
    lateinit var mSwipeRefreshLayout: MyRefreshLayout<Joke>
    private var count = 0
    private var jokeList: ArrayList<Joke> = ArrayList()
    private lateinit var mAdapter: HeaderAndFooterAdapter
    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            STATE_PULL_TO_DOWM_REFRESH -> {
                mAdapter!!.notifyDataSetChanged()
                mSwipeRefreshLayout!!.setRefreshing(false)
            }
            STATE_PULL_TO_UP_REFRESH -> mAdapter!!.notifyDataSetChanged()
        }
        false
    })
    private var protocol: JokeProtocol? = null

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun createSuccessView(): View {
        val view = View.inflate(context, R.layout.refresh_layout, null)
        mRecycleView=view.findViewById<RecyclerView>(R.id.rv)
        mSwipeRefreshLayout=view.findViewById<MyRefreshLayout<Joke>>(R.id.srl)
        jokeList= protocol!!.getList(0) as ArrayList<Joke>
        val defaultAdapter = JokeAdapter(this!!.context!!, jokeList)
        mAdapter = HeaderAndFooterAdapter(defaultAdapter)
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN, Color.YELLOW, Color.RED)
        mRecycleView.layoutManager = LinearLayoutManager(context)
        mRecycleView.adapter = mAdapter
        //item之间分割线
        mRecycleView.addItemDecoration(DividerItemDecoration(this!!.activity!!, DividerItemDecoration.VERTICAL_LIST))
        //下拉刷新监听
        mSwipeRefreshLayout.setOnRefreshListener(this)
        //上拉加载更多监听
        mSwipeRefreshLayout.setPullToUpRefreshListener(this)
        return view
    }

    override fun load(): LoadingPage.LoadResult {
        val url=NewsURL.getVideoUrl()
        Log.v("aaaaaaaaaaaa",url)
        System.out.print(url)
        protocol = JokeProtocol("",url , context, "normal")
        val jokeList = protocol!!.load()
        return checkData(jokeList)
    }

    override fun onPause() {
        super.onPause()
        JCVideoPlayer.releaseAllVideos()
    }

    /**
     * 下拉刷新监听
     */
    override fun onRefresh() {
        Thread{ run {
            CacheUtils.removeKey(NewsURL.BAISIBUDEJIE_JOKE_HTTP)
            val url=NewsURL.getVideoUrl()
            System.out.print(url)
            protocol = JokeProtocol("", url, context, "normal")
            protocol!!.load()
            jokeList!!.clear()
            jokeList!!.addAll(protocol!!.getList(0)!!)
            val message = Message.obtain()
            message.what = STATE_PULL_TO_DOWM_REFRESH
            mHandler.sendMessage(message)
        } }.start()
    }

    /**
     * 此处在子线程运行，如果请求的数据有更多，则跳转到hasMore，hasMore在主线程运行
     */
    override fun onLoad(): List<Joke>? {
        count++
        var newData: List<Joke>? = null
        if (count < protocol!!.list.size)
            newData = protocol!!.getList(count)
        return newData
    }

    /**
     * 此处在主线程运行
     * @param newData
     */
    override fun hasMore(newData: List<Joke>) {
        jokeList!!.addAll(newData)
        val message = Message.obtain()
        message.what = STATE_PULL_TO_UP_REFRESH
        mHandler.sendMessage(message)
    }

    companion object {
        private val STATE_PULL_TO_DOWM_REFRESH = 0
        private val STATE_PULL_TO_UP_REFRESH = 1
    }
}
