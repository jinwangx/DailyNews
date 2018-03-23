package com.jw.dailyNews.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.jw.dailyNews.R
import com.jw.dailyNews.activity.ArticleActivity
import com.jw.dailyNews.activity.ImageActivity
import com.jw.dailyNews.adapter.DefaultAdapter
import com.jw.dailyNews.adapter.HeaderAndFooterAdapter
import com.jw.dailyNews.adapter.NewsObjectAdapter
import com.jw.dailyNews.adapter.TopNewsAdapter
import com.jw.dailyNews.base.BaseFragment
import com.jw.dailyNews.bean.NewsObject
import com.jw.dailyNews.protocol.NewsObjectProtocol
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.utils.CommonUtils
import com.jw.dailyNews.wiget.LoadingPage
import com.jw.dailyNews.wiget.MyRefreshLayout
import com.viewpagerindicator.CirclePageIndicator

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 15:53
 * 版本：
 * 作者：Mr.jin
 * 描述：推荐新闻页面
 */

class NewsTabObject : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
        MyRefreshLayout.PullToUpRefreshListener<NewsObject.News>,
        ViewPager.OnPageChangeListener, DefaultAdapter.OnItemClickListener<NewsObject.News> {
    private var count = 0
    private lateinit var topTitle: TextView
    private lateinit var newsList: ArrayList<NewsObject.News>
    private lateinit var focus: ArrayList<NewsObject.News>
    private lateinit var indicator: CirclePageIndicator
    private val links = ArrayList<String>()
    private val urls = ArrayList<String>()
    private val descs = ArrayList<String>()
    private lateinit var mAdapter: HeaderAndFooterAdapter
    private lateinit var swipeRefreshLayout: MyRefreshLayout<*>
    private lateinit var topNewsAdapter: TopNewsAdapter
    private var currentPosition = 0
    private lateinit var viewpager: ViewPager


    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
        //下拉刷新，数据更新完毕，界面开始更新
            STATE_PULL_TO_DOWM_REFRESH -> {
                /*                    topNewsAdapter = new TopNewsAdapter(getContext(),links, descs, urls);
                    viewpager.setAdapter(topNewsAdapter);*/
                topNewsAdapter!!.notifyDataSetChanged()
                indicator!!.setCurrentItem(currentPosition)
                mAdapter!!.notifyDataSetChanged()
                swipeRefreshLayout!!.setRefreshing(false)
            }
        //上拉加载更多，数据更新完毕，界面开始更新
            STATE_PULL_TO_UP_REFRESH -> {
                mAdapter!!.notifyDataSetChanged()
                swipeRefreshLayout!!.setLoading(false)
            }
        //轮播图开始滚动
            /*STATE_IMAGE_CIRCLE -> mHandler.postDelayed(Runnable {
                currentPosition++
                if (currentPosition == descs.size) {
                    currentPosition = 0
                }
                indicator!!.setCurrentItem(currentPosition)
                //自循环
                val message = Message.obtain()
                message.what = STATE_IMAGE_CIRCLE
                mHandler.sendMessage(message)
            }, 4000)*/
        }
        false
    })

    override fun createSuccessView(): View {
        val view = View.inflate(activity, R.layout.refresh_layout, null)
        //自己封装的带有下拉刷新和下拉加载更多功能的SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById<MyRefreshLayout<*>>(R.id.srl)
        val recycleView = view.findViewById<RecyclerView>(R.id.rv)
        val recycleAdapter = NewsObjectAdapter(this!!.activity!!, newsList)
        //RecycleAdapter的装饰类，使recycleView能够添加多个headView和footView
        mAdapter = HeaderAndFooterAdapter(recycleAdapter)
        mAdapter!!.addHeaderView(initHeaderView())
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = mAdapter
        recycleAdapter.setOnItemClickListener(this)
        //给swipeRefreshLayout设置下拉刷新progressBar颜色效果
        swipeRefreshLayout!!.setColorSchemeColors(Color.BLUE,
                Color.GREEN, Color.YELLOW, Color.RED)
        swipeRefreshLayout!!.setOnRefreshListener(this)
        swipeRefreshLayout!!.setPullToUpRefreshListener(this)
        return view
    }

    /**
     * 请求服务器 获取服务器的数据
     */
    override fun load(): LoadingPage.LoadResult {
        val protocol = NewsObjectProtocol("",
                CommonUtils.createRecommondUrl(count), context, "news")
        if (protocol.load() != null) {
            newsList = protocol.newsList!!
            focus = protocol.focus!!
        }
        return checkData(newsList) // 检查数据 有三种结果  成功, 错误,空
    }


    private fun initHeaderView(): View {
        val headerView = View.inflate(activity, R.layout.top_news, null)
        viewpager = headerView.findViewById<ViewPager>(R.id.vpTopNews)
        indicator = headerView.findViewById<CirclePageIndicator>(R.id.cpiTopNews)
        topTitle = headerView.findViewById<TextView>(R.id.tvTopNews)

        for (news in focus!!) {
            if (news.imgsrc3gtype == 2) {
                links.add(news.picInfo!![0].url!!)
                descs.add(news.title!!)
                urls.add(news.link!!)
            }
        }
        topNewsAdapter = TopNewsAdapter(this!!.context!!, links, descs, urls)
        viewpager!!.adapter = topNewsAdapter
        viewpager!!.addOnPageChangeListener(this)
        indicator!!.setViewPager(viewpager)
        indicator!!.setCurrentItem(currentPosition)
        //        topTitle.setText(descs.get(currentPosition));
        val message = Message.obtain()
        message.what = STATE_IMAGE_CIRCLE
        mHandler.sendMessage(message)
        return headerView
    }

    override fun onItemClick(v: View, position: Int, news: NewsObject.News) {
        val url = news.link
        Log.v("ObjectUrl", url)
        //跳转到图片信息页面
        if (url!!.contains("photoview")) {
            val intent = Intent(activity, ImageActivity::class.java)
            intent.putExtra("docurl", url)
            activity?.startActivity(intent)
        }//跳转到文章页面
        else if (url.contains("news") || url.contains("article")) {
            val intent = Intent(activity, ArticleActivity::class.java)
            intent.putExtra("docurl", url)
            activity?.startActivity(intent)
        }
    }

    /**
     * 下拉刷新监听
     */
    override fun onRefresh() {
        Thread{  run {
            for (i in 0..count)
                CacheUtils.removeKey(CommonUtils.createRecommondUrl(count))
            count=0
            val protocol = NewsObjectProtocol("",
                    CommonUtils.createRecommondUrl(count), context, "news")
            //清空所有list中的数据然后加载刷新读到的数据
            if (protocol.load() != null) {
                newsList!!.clear()
                newsList!!.addAll(protocol.newsList)
                focus!!.clear()
                focus!!.addAll(protocol.focus!!)
            }
            links.clear()
            descs.clear()
            urls.clear()
            for (news in focus!!)
                if (news.imgsrc3gtype == 2) {
                    links.add(news.picInfo!![0].url!!)
                    descs.add(news.title!!)
                    urls.add(news.link!!)
                }
            val message = Message.obtain()
            message.what = STATE_PULL_TO_DOWM_REFRESH
            mHandler.sendMessage(message)
            currentPosition = -1
        } }.start()
    }

    /**
     * 此处在子线程运行，如果请求的数据有更多，则跳转到hasMore，hasMore在主线程运行
     */
    override fun onLoad()=NewsObjectProtocol("",
                CommonUtils.createRecommondUrl(++count), context, "news").load()!!.list


    /**
     * 此处在主线程运行
     * @param newData
     */
    override fun hasMore(newData: List<NewsObject.News>) {
        newsList!!.addAll(newData)
        val message = Message.obtain()
        message.what = STATE_PULL_TO_UP_REFRESH
        mHandler.sendMessage(message)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    //更新当前轮播图所在item对应的文字说明
    override fun onPageSelected(position: Int) {
        topTitle!!.text = descs[position]
        currentPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    companion object {
        //下拉刷新状态
        private val STATE_PULL_TO_DOWM_REFRESH = 0
        //上拉加载更多状态
        private val STATE_PULL_TO_UP_REFRESH = 1
        //轮播图开始滚动状态
        private val STATE_IMAGE_CIRCLE = 2
    }
}
