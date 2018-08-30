package com.jw.dailyNews.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jw.dailyNews.R
import com.jw.dailyNews.bean.NewsNormal
import com.jw.dailyNews.utils.DateUtils
import com.jw.dailyNews.utils.GlideUtils

/**
 * 创建时间：2017/7/31
 * 更新时间：2017/11/12 0012 上午 2:33
 * 作者：Mr.jin
 * 描述：
 */
class NewsNormalAdapter(context: Context, lists: List<NewsNormal>) : DefaultAdapter<NewsNormal>(context, lists) {

    enum class ITEM_TYPE {
        ITEM_TYPE_0,
        ITEM_TYPE_1,
        ITEM_TYPE_2,
        ITEM_TYPE_3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_0.ordinal) {
            val view = mInflater.inflate(R.layout.item_null, parent, false)
            return NewsNormalAdapter.Holder0(view)
        } else if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_1.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type1, parent, false)
            return NewsNormalAdapter.Holder1(view)
        } else if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_2.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type2, parent, false)
            return NewsNormalAdapter.Holder2(view)
        } else if (viewType == NewsNormalAdapter.ITEM_TYPE.ITEM_TYPE_3.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type3, parent, false)
            return NewsNormalAdapter.Holder3(view)
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val news = lists[position]
        if (holder is Holder0) {

        } else if (holder is Holder1) {
            holder.titleType1.text = news.title
            GlideUtils.load(mContext, news.imgsrc, holder.imageType1)
            holder.categoryType1.text = news.source
            holder.timeType1.text = news.ptime
            holder.tcountType1.text = news.commentCount.toString() + ""
        } else if (holder is Holder2) {
            holder.titleType2.text = news.title
            GlideUtils.load(mContext, news.imgsrc, holder.image1Type2)
            GlideUtils.load(mContext, news.imgextra!![0].imgsrc, holder.image2Type2)
            GlideUtils.load(mContext, news.imgextra!![1].imgsrc, holder.image3Type2)

            holder.categoryType2.text = news.source
            holder.timeType2.text = DateUtils.fromNow("yyyy-MM-dd HH:mm:ss", news.ptime)
            holder.tcountType2.text = news.commentCount.toString() + ""
        } else if (holder is Holder3) {
            holder.titleType3.text = news.title
            GlideUtils.load(mContext, news.imgsrc, holder.imageType3)
            holder.categoryType3.text = news.source
            holder.timeType3.text = DateUtils.fromNow("yyyy-MM-dd HH:mm:ss", news.ptime)
            holder.tcountType3.text = news.commentCount.toString() + ""
        }
    }

    override fun getItemViewType(position: Int): Int {
        val news = lists[position]
        var gtype = news.imgsrc3gtype
        val source = lists[position].source!! + ""
        if (source.contains("网易") || source.contains("编辑"))
            gtype = "0"
        var ordinal = 0
        when (gtype) {
            "0" -> ordinal = ITEM_TYPE.ITEM_TYPE_0.ordinal
            "1" -> ordinal = ITEM_TYPE.ITEM_TYPE_1.ordinal
            "2" -> ordinal = ITEM_TYPE.ITEM_TYPE_2.ordinal
            "3" -> ordinal = ITEM_TYPE.ITEM_TYPE_3.ordinal
        }
        return ordinal
    }

    class Holder0(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

        }
    }

    class Holder1(itemView: View) : BaseHolder(itemView) {
        internal var imageType1 = itemView.findViewById<ImageView>(R.id.image_type1)
        internal var titleType1 = itemView.findViewById<TextView>(R.id.title_type1)
        internal var categoryType1 = itemView.findViewById<TextView>(R.id.category_type1)
        internal var timeType1 = itemView.findViewById<TextView>(R.id.time_type1)
        internal var tcountType1 = itemView.findViewById<TextView>(R.id.tcount_type1)
    }

    class Holder2(itemView: View) : BaseHolder(itemView) {
        internal var titleType2 = itemView.findViewById<TextView>(R.id.title_type2)
        internal var image1Type2 = itemView.findViewById<ImageView>(R.id.image1_type2)
        internal var image2Type2 = itemView.findViewById<ImageView>(R.id.image2_type2)
        internal var image3Type2 = itemView.findViewById<ImageView>(R.id.image3_type2)
        internal var categoryType2 = itemView.findViewById<TextView>(R.id.category_type2)
        internal var timeType2 = itemView.findViewById<TextView>(R.id.time_type2)
        internal var tcountType2 = itemView.findViewById<TextView>(R.id.tcount_type2)
    }

    class Holder3(itemView: View) : BaseHolder(itemView) {
        internal var titleType3 = itemView.findViewById<TextView>(R.id.title_type3)
        internal var imageType3 = itemView.findViewById<ImageView>(R.id.image_type3)
        internal var categoryType3 = itemView.findViewById<TextView>(R.id.category_type3)
        internal var timeType3 = itemView.findViewById<TextView>(R.id.time_type3)
        internal var tcountType3 = itemView.findViewById<TextView>(R.id.tcount_type3)
    }

    open class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

        }
    }
}
