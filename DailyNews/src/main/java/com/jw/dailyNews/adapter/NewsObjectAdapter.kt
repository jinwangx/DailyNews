package com.jw.dailyNews.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jw.dailyNews.R
import com.jw.dailyNews.bean.NewsObject
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == ITEM_TYPE.ITEM_TYPE_0.ordinal) {
            val view = mInflater.inflate(R.layout.item_null, parent, false)
            return Holder0(view)
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_1.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type1, parent, false)
            return Holder1(view)
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_2.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type2, parent, false)
            return Holder2(view)
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_3.ordinal) {
            val view = mInflater.inflate(R.layout.item_list_img_type3, parent, false)
            return Holder3(view)
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val news = lists[position]
        if (holder is Holder0) {

        } else if (holder is Holder1) {
            holder.titleType1!!.text = news.title
            GlideUtils.load(mContext, news.picInfo!![0].url, holder.imageType1)
            holder.categoryType1!!.text = news.source
            holder.timeType1!!.text = news.ptime
            holder.tcountType1!!.text = news.tcount.toString() + ""
        } else if (holder is Holder2) {
            holder.titleType2!!.text = news.title
            GlideUtils.load(mContext, news.picInfo!![0].url, holder.image1Type2)
            GlideUtils.load(mContext, news.picInfo!![1].url, holder.image2Type2)
            GlideUtils.load(mContext, news.picInfo!![2].url, holder.image3Type2)
            holder.categoryType2!!.text = news.source
            holder.timeType2!!.text = DateUtils.fromNow("yyyy-MM-dd HH:mm:ss", news.ptime)
            holder.tcountType2!!.text = news.tcount.toString() + ""
        } else if (holder is Holder3) {
            holder.titleType3!!.text = news.title
            GlideUtils.load(mContext, news.picInfo!![0].url, holder.imageType3)
            holder.categoryType3!!.text = news.source
            holder.timeType3!!.text = DateUtils.fromNow("yyyy-MM-dd HH:mm:ss", news.ptime)
            holder.tcountType3!!.text = news.tcount.toString() + ""
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
        var imageType1:ImageView=itemView.findViewById(R.id.image_type1)
        var titleType1:TextView=itemView.findViewById(R.id.title_type1)
        var categoryType1:TextView=itemView.findViewById(R.id.category_type1)
        var timeType1:TextView=itemView.findViewById(R.id.time_type1)
        var tcountType1:TextView=itemView.findViewById(R.id.tcount_type1)
    }

    class Holder2(itemView: View) : BaseHolder(itemView) {
        var titleType2: TextView =itemView.findViewById(R.id.title_type2)
        var image1Type2:ImageView=itemView.findViewById(R.id.image1_type2)
        var image2Type2:ImageView=itemView.findViewById(R.id.image2_type2)
        var image3Type2:ImageView=itemView.findViewById(R.id.image3_type2)
        var categoryType2:TextView=itemView.findViewById(R.id.category_type2)
        var timeType2:TextView=itemView.findViewById(R.id.time_type2)
        var tcountType2:TextView=itemView.findViewById(R.id.tcount_type2)
    }

    class Holder3(itemView: View) : BaseHolder(itemView) {
        var titleType3:TextView=itemView.findViewById(R.id.title_type3)
        var imageType3:ImageView=itemView.findViewById(R.id.image_type3)
        var categoryType3:TextView=itemView.findViewById(R.id.category_type3)
        var timeType3:TextView=itemView.findViewById(R.id.time_type3)
        var tcountType3:TextView=itemView.findViewById(R.id.tcount_type3)
    }

    open class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
        }
    }
}
