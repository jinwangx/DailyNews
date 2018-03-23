package com.jw.dailyNews.wiget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.jw.dailyNews.R

/**
 * 创建时间：2017/7/28
 * 更新时间：2017/11/11 0011 上午 12:45
 * 作者：Mr.jin
 * 描述：主界面左面板正文字号item组合控件
 */
class ItemArrowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    lateinit var ivIcon: ImageView
    lateinit var tvContent: TextView
    private val iconResource: Int
    private val desc: String?
    private val dividerHeight: Float
    private val dividerColor: Int
    private var dividerLine: View? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.itemArrowView)
        iconResource = ta.getResourceId(R.styleable.itemArrowView_icon, 0)
        desc = ta.getString(R.styleable.itemArrowView_content)
        dividerHeight = ta.getDimension(R.styleable.itemArrowView_dividerHeight, 2f)
        dividerColor = ta.getColor(R.styleable.itemArrowView_dividerColor, android.R.color.holo_orange_light)
        ta.recycle()
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.wight_arrow_view, this)
        ivIcon = view.findViewById<View>(R.id.ivIconWav) as ImageView
        tvContent = view.findViewById<View>(R.id.tvContentWav) as TextView
        dividerLine = view.findViewById(R.id.divider_line)
        ivIcon.setImageResource(iconResource)
        tvContent.text = desc
        dividerLine!!.setBackgroundColor(dividerColor)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
