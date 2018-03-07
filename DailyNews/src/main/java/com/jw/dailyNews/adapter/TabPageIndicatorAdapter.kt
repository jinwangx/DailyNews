package com.jw.dailyNews.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.jw.dailyNews.fragment.FragmentFactory

/**
 * 创建时间：2017/7/27
 * 更新时间：2017/11/11 0011 上午 12:24
 * 作者：Mr.jin
 * 描述：新闻首页页面TabPageIndicator适配器
 */
class TabPageIndicatorAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val titles= FragmentFactory.get().titles

    //新建一个Fragment来展示ViewPager item的内容，并传递参数
    override fun getItem(position: Int)=FragmentFactory.get().createFragment(position)

    override fun getPageTitle(position: Int)=titles[position % titles.size]

    override fun getCount()=titles.size

}
