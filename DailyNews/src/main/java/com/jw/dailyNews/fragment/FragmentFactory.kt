package com.jw.dailyNews.fragment

import Lib.NewsURL
import android.support.v4.app.Fragment
import com.jw.dailyNews.utils.CommonUtils
import java.util.*

/**
 * 创建时间：2017/6/20
 * 更新时间：2017/11/11 0011 上午 12:30
 * 作者：Mr.jin
 * 描述：首页新闻页面各板块fragment工厂
 */
class FragmentFactory private constructor() {
    private val mFragments = HashMap<Int, Fragment>()
    private var urls: MutableList<String>?=null
    lateinit var titles: Array<String>

    init {
        if (urls == null) {
            urls = ArrayList()
            urls!!.add(CommonUtils.createRecommondUrl(0))
            urls!!.add(NewsURL.URL_HTTP_GUONEI)
            urls!!.add(NewsURL.URL_HTTP_WORLD)
            urls!!.add(NewsURL.URL_HTTP_SHEHUI)
            urls!!.add(NewsURL.URL_HTTP_HISTORY)
            urls!!.add(NewsURL.URL_HTTP_ENTERTAINMENT)
            urls!!.add(NewsURL.URL_HTTP_ECONOMIC)

            urls!!.add(NewsURL.URL_HTTP_EDU)
            urls!!.add(NewsURL.URL_HTTP_GAME)
            urls!!.add(NewsURL.URL_HTTP_WAR)
            urls!!.add(NewsURL.URL_HTTP_SPORTS)
            urls!!.add(NewsURL.URL_HTTP_OWN)


            titles = arrayOf("推荐", "国内", "国际", "社会", "历史", "娱乐", "财经", "教育", "游戏", "军事", "体育", "独家")
        }
    }

    fun createFragment(position: Int): Fragment? {
        var fragment = mFragments[position]
        if (fragment == null) {
            if (position == 0) {
                fragment = NewsTabObject()
            } else {
                fragment = NewsTabNormal.newInstance(urls!![position])

            }
            if (fragment != null)
                mFragments.put(position, fragment)
        }
        return fragment
    }

    companion object {
        fun get()=Inner.anotherSingle
    }

    private object Inner{
        var anotherSingle=FragmentFactory()
    }
}
