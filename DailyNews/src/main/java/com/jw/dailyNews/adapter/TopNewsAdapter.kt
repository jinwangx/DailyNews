package com.jw.dailyNews.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.jw.dailyNews.activity.ImageActivity

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 上午 12:26
 * 作者：Mr.jin
 * 描述：新闻首页推荐新闻部分大图轮播部分adapter
 */
class TopNewsAdapter(private val context: Context, private val links: List<String>
                     , private val descs: List<String>, private val urls: List<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return links.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val image = ImageView(context)
        image.scaleType = ImageView.ScaleType.FIT_XY// 基于控件大小填充图片
        Glide.with(context).load(links[position]).into(image) // 传递imagView对象和图片地址
        image.setOnClickListener {
            val intent = Intent()
            intent.putExtra("docurl", urls[position])
            intent.setClass(context, ImageActivity::class.java)
            context.startActivity(intent)
        }
        container.addView(image)
        return image
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}