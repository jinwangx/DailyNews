package com.jw.dailyNews.wiget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.jw.dailyNews.R

/**
 * 创建时间：2017/7/28
 * 更新时间：2017/11/11 0011 上午 12:45
 * 作者：Mr.jin
 * 描述：主界面左面板清除缓存item组合控件
 */
class ItemTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    lateinit var tvTitle: TextView
    lateinit var tvContent: TextView
    private val desc: String?
    private val title: String?
    private val color: Int
    private var dividerColor: View? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.itemTextView)
        title = ta.getString(R.styleable.itemTextView_title3)
        desc = ta.getString(R.styleable.itemTextView_desc3)
        color = ta.getColor(R.styleable.itemTextView_dividerColor3, android.R.color.holo_orange_light)
        ta.recycle()
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.wight_text_view, this)
        tvTitle = view.findViewById<View>(R.id.tvTitleWtv) as TextView
        tvContent = view.findViewById<View>(R.id.tvDescWtv) as TextView
        dividerColor = view.findViewById(R.id.divider_color3)
        tvTitle.text = title
        tvContent.text = desc
        dividerColor!!.setBackgroundColor(color)
    }

    fun setText(text: String) {
        tvContent.text = text
    }

}
