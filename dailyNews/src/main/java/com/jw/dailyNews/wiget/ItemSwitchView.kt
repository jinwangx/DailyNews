package com.jw.dailyNews.wiget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView

import com.jw.dailyNews.R

/**
 * 创建时间：2017/7/28
 * 更新时间：2017/11/11 0011 下午 6:09
 * 作者：Mr.jin
 * 描述：主界面结合switchButton的组合控件
 */
class ItemSwitchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    lateinit var tvContent: TextView
    private val dividerColor: Int
    internal var title: String? = null
    private var divider: View? = null
    private var mListener: SwitchListener? = null
    private var switchButton: SwitchButton? = null
    private var order: Int = 0

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.itemSwitchView)
        title = ta.getString(R.styleable.itemSwitchView_content2)
        dividerColor = ta.getColor(R.styleable.itemSwitchView_dividerColor2, android.R.color.holo_orange_light)
        ta.recycle()
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.wight_switch_view, this)
        divider = view.findViewById(R.id.divider_line)
        tvContent = view.findViewById<View>(R.id.tvContentWsv) as TextView
        switchButton = view.findViewById<View>(R.id.switchButton) as SwitchButton
        tvContent.text = title
        divider!!.setBackgroundColor(dividerColor)
        switchButton!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                mListener!!.onOpen(order)
            else
                mListener!!.onClose(order)
        }
    }

    fun setChecked(ifChecked: Boolean) {
        switchButton!!.isChecked = ifChecked
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setSwitchListener(listener: SwitchListener, order: Int) {
        this.mListener = listener
        this.order = order
    }

    interface SwitchListener {
        fun onOpen(order: Int)
        fun onClose(order: Int)
    }
}
