package com.jw.dailyNews.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.databinding.library.baseAdapters.BR
import com.jw.dailyNews.R
import com.jw.dailyNews.bean.NewsObject
import com.jw.dailyNews.databinding.ItemListImgType1Binding
import com.jw.dailyNews.databinding.ItemListImgType1ObjectBinding
import com.jw.dailyNews.databinding.ItemListImgType2ObjectBinding
import com.jw.dailyNews.databinding.ItemListImgType3ObjectBinding
import com.jw.dailyNews.utils.DateUtils
import com.jw.dailyNews.utils.GlideUtils

/**
 * 创建时间：2017/7/31
 * 更新时间：2017/11/12 0012 上午 2:34
 * 作者：Mr.jin
 * 描述：
 */
class NewsObjectAdapter(context: Context, mNewsList: List<NewsObject.News>) : DefaultAdapter<NewsObject.News>(context, mNewsList) {

    enum class ITEM_TYPE {
        ITEM_TYPE_0,
        ITEM_TYPE_1,
        ITEM_TYPE_2,
        ITEM_TYPE_3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_TYPE.ITEM_TYPE_0.ordinal) {
            val view = mInflater.inflate(R.layout.item_null, parent, false)
            return Holder0(view)
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_1.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type1_object, parent, false)
            return Holder1(view)
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_2.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type2_object, parent, false)
            return Holder2(view)
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_3.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type3_object, parent, false)
            return Holder3(view)
        }
        return Holder0(ImageView(mContext))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val news = lists[position]
        if (holder is Holder0) {

        } else if (holder is Holder1) {
            val binding = DataBindingUtil.bind<ItemListImgType1ObjectBinding>(holder.itemView)
            binding!!.setVariable(BR.news,news)
        } else if (holder is Holder2) {
            val binding = DataBindingUtil.bind<ItemListImgType2ObjectBinding>(holder.itemView)
            binding!!.setVariable(BR.news,news)
        } else if (holder is Holder3) {
            val binding = DataBindingUtil.bind<ItemListImgType3ObjectBinding>(holder.itemView)
            binding!!.setVariable(BR.news,news)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val news = lists[position]
        var gtype = news.imgsrc3gtype
        val source = lists[position]?.source + ""
        if (source.contains("网易") || source.contains("编辑"))
            gtype = ITEM_TYPE.ITEM_TYPE_0.ordinal
        var ordinal = 0
        when (gtype) {
            0 -> ordinal = ITEM_TYPE.ITEM_TYPE_0.ordinal
            1 -> ordinal = ITEM_TYPE.ITEM_TYPE_1.ordinal
            2 -> ordinal = ITEM_TYPE.ITEM_TYPE_2.ordinal
            3 -> ordinal = ITEM_TYPE.ITEM_TYPE_3.ordinal
        }
        return ordinal
    }

    class Holder0(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
        }
    }

    class Holder1(itemView: View) : BaseHolder(itemView) {
    }

    class Holder2(itemView: View) : BaseHolder(itemView) {
    }

    class Holder3(itemView: View) : BaseHolder(itemView) {
    }

    open class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
        }
    }
}
