package com.jw.dailyNews.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * 创建时间：
 * 更新时间 2017/10/30 15:45
 * 版本：
 * 作者：Mr.jin
 * 描述：装饰类Adapter，可以为adapter装饰header和footer
 */

class HeaderAndFooterAdapter(private val mInnerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mHeaderViews = SparseArrayCompat<View>()
    private val mFootViews = SparseArrayCompat<View>()

    val headersCount: Int
        get() = mHeaderViews.size()

    val footersCount: Int
        get() = mFootViews.size()

    private val realItemCount: Int
        get() = mInnerAdapter.itemCount

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + realItemCount
    }


    fun addHeaderView(view: View) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view)
    }

    fun addFootView(view: View) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view)
        notifyDataSetChanged()
    }

    fun removeFootView() {
        mFootViews.remove(mFootViews.size() + BASE_ITEM_TYPE_FOOTER)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mHeaderViews.get(viewType) != null) {
            return object : RecyclerView.ViewHolder(mHeaderViews.get(viewType)) {
                override fun toString(): String {
                    return super.toString()
                }
            }

        } else if (mFootViews.get(viewType) != null) {
            return object : RecyclerView.ViewHolder(mFootViews.get(viewType)) {
                override fun toString(): String {
                    return super.toString()
                }
            }
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - realItemCount)
        }
        return mInnerAdapter.getItemViewType(position - headersCount)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        }
        if (isFooterViewPos(position)) {
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position - headersCount)
    }

    override fun getItemCount(): Int {
        return headersCount + footersCount + realItemCount
    }

    companion object {
        private val BASE_ITEM_TYPE_HEADER = 100000
        private val BASE_ITEM_TYPE_FOOTER = 200000
    }
}