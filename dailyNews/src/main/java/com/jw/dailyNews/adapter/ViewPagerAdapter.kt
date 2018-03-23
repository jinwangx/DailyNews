package com.jw.dailyNews.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 上午 12:28
 * 作者：Mr.jin
 * 描述：图片新闻viewpager适配器
 */
class ViewPagerAdapter(private val views: List<View>) : PagerAdapter() {

    override fun getCount()=this.views.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(views[position])
        return views[position]
    }

    override fun isViewFromObject(view: View, obj: Any)=view === obj

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }
}