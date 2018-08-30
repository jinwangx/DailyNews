package com.jw.dailyNews.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.jw.dailyNews.R
import com.jw.dailyNews.activity.ArticleActivity
import com.jw.dailyNews.activity.ImageActivity
import com.jw.dailyNews.adapter.DefaultAdapter
import com.jw.dailyNews.adapter.HeaderAndFooterAdapter
import com.jw.dailyNews.adapter.NewsNormalAdapter
import com.jw.dailyNews.base.BaseFragment
import com.jw.dailyNews.bean.NewsNormal
import com.jw.dailyNews.protocol.NewsNormalProtocol
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.utils.CommonUtils
import com.jw.dailyNews.wiget.LoadingPage
import com.jw.dailyNews.wiget.MyRefreshLayout

/**
 * 创建时间：2017/7/18
 * 更新时间 2017/10/30 15:53
 * 版本：
 * 作者：Mr.jin
 * 描述：普通新闻页面
 */

class NewsTabNormal : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
        MyRefreshLayout.PullToUpRefreshListener<NewsNormal>, DefaultAdapter.OnItemClickListener<NewsNormal> {
    lateinit var recycleView: RecyclerView
    lateinit var swipeRefreshLayout: MyRefreshLayout<NewsNormal>
    private lateinit var newsNormalList: ArrayList<NewsNormal>
    private var count = 0
    private lateinit var url: String
    private var mAdapter: HeaderAndFooterAdapter? = null
    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            STATE_PULL_TO_DOWM_REFRESH -> {
                mAdapter!!.notifyDataSetChanged()
                swipeRefreshLayout?.setRefreshing(false)
            }
            STATE_PULL_TO_UP_REFRESH -> {
                mAdapter!!.notifyDataSetChanged()
                swipeRefreshLayout?.setLoading(false)
            }
        }
        false
    })

    override fun createSuccessView(): View {
        val view = View.inflate(activity, R.layout.refresh_layout, null)
        swipeRefreshLayout = view.findViewById<MyRefreshLayout<NewsNormal>>(R.id.srl)
        recycleView = view.findViewById<RecyclerView>(R.id.rv)
        val recycleAdapter = NewsNormalAdapter(this!!.activity!!, newsNormalList)
        recycleAdapter.setOnItemClickListener(this)
        mAdapter = HeaderAndFooterAdapter(recycleAdapter)
        recycleView?.layoutManager = LinearLayoutManager(context)
        recycleView?.adapter = mAdapter
        swipeRefreshLayout?.setColorSchemeColors(Color.BLUE,
                Color.GREEN, Color.YELLOW, Color.RED)
        swipeRefreshLayout?.setOnRefreshListener(this)
        swipeRefreshLayout?.setPullToUpRefreshListener(this)
        return view
    }

    override fun load(): LoadingPage.LoadResult {
        val arguments = arguments
        url = arguments?.getString("url")!!
        val protocol = NewsNormalProtocol(url, CommonUtils.createNewsUrl(url, 0), this!!.context!!, "news")
        newsNormalList = protocol.load() as ArrayList<NewsNormal>
        return checkData(newsNormalList)
    }

    override fun onItemClick(v: View, position: Int, news: NewsNormal) {
        val url: String?
        if (news.skipURL != null)
            url = news.skipURL
        else
            url = news.url
        Log.v("normalNewsUrl", url)
        if (url!!.contains("photoview")) {
            val intent = Intent(activity, ImageActivity::class.java)
            intent.putExtra("docurl", url)
            activity?.startActivity(intent)
        } else if (url.contains("news") || url.contains("article")) {
            val intent = Intent(activity, ArticleActivity::class.java)
            intent.putExtra("docurl", url)
            activity?.startActivity(intent)
        } else {
            val intent = Intent(activity, ArticleActivity::class.java)
            intent.putExtra("docurl", url)
            activity?.startActivity(intent)
        }
    }

    /**
     * 下拉刷新监听
     */
    override fun onRefresh() {
        for (i in 0..count)
            CacheUtils.removeKey(CommonUtils.createNewsUrl(url, count))
        Thread{ run {
            count=0
            val protocol = NewsNormalProtocol(url, CommonUtils.createNewsUrl(url, count), this!!.context!!, "news")
            newsNormalList!!.clear()
            newsNormalList!!.addAll(protocol.load()!!)
            val message = Message.obtain()
            message.what = STATE_PULL_TO_DOWM_REFRESH
            mHandler.sendMessage(message)
        } }.start()
    }

    /**
     * 此处在子线程运行，如果请求的数据有更多，则跳转到hasMore，hasMore在主线程运行
     */
    override fun onLoad()=NewsNormalProtocol(url,
                CommonUtils.createNewsUrl(url, ++count), this!!.context!!, "news").load()

    /**
     * 此处在主线程运行
     * @param newData
     */
    override fun hasMore(newData: List<NewsNormal>) {
        newsNormalList!!.addAll(newData)
        val message = Message.obtain()
        message.what = STATE_PULL_TO_DOWM_REFRESH
        mHandler.sendMessage(message)
    }

    companion object {
        private val STATE_PULL_TO_DOWM_REFRESH = 0
        private val STATE_PULL_TO_UP_REFRESH = 1

        //外界通过newInstance的方式来获取NewsTabNormal实例
        fun newInstance(url: String): NewsTabNormal {
            val newsTabNormal = NewsTabNormal()
            val bundle = Bundle()
            bundle.putString("url", url)
            newsTabNormal.arguments = bundle
            return newsTabNormal
        }
    }
}
