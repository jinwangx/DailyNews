package com.jw.dailyNews.fragment

import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import com.jw.dailyNews.R
import com.jw.dailyNews.adapter.TabPageIndicatorAdapter
import com.jw.dailyNews.base.BaseFragment
import com.jw.dailyNews.utils.ThemeUtils
import com.jw.dailyNews.wiget.LoadingPage
import com.viewpagerindicator.TabPageIndicator
import kotlinx.android.synthetic.main.fragment_shouye.*

/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:52
 * 版本：
 * 作者：Mr.jin
 * 描述：主界面的首页界面，其中内嵌多个新闻fragment，默认页面为推荐新闻页面,NewsTabObject,
 * 其他页面类型均为NewsTabNormal
 */

class FragmentShouye : BaseFragment(), View.OnClickListener {

    private var popupWindow: PopupWindow? = null
    private var vv: View? = null
    private var popWindowHeight: Int = 0
    private var popWindowWidth: Int = 0
    private lateinit var dropDownAnim: Animation
    private lateinit var dropUpAnim: Animation

    override fun createSuccessView(): View {
        val view = View.inflate(activity, R.layout.fragment_shouye, null)
        val ibAdd = view.findViewById<ImageButton>(R.id.ibAdd) as ImageButton
        ibAdd.setOnClickListener(this)
        val adapter = TabPageIndicatorAdapter(childFragmentManager)
        val vpShouye = view.findViewById<ViewPager>(R.id.vpShouye)
        val tpiShouye = view.findViewById<TabLayout>(R.id.tpiShouye)
        val flShouye = view.findViewById<FrameLayout>(R.id.flShouye)
        vpShouye.adapter=adapter
        tpiShouye.tabMode = TabLayout.MODE_SCROLLABLE;
        tpiShouye.setTabTextColors(ContextCompat.getColor(activity!!, R.color.gray), ContextCompat.getColor(activity!!, R.color.red))
        tpiShouye.setSelectedTabIndicatorColor(ContextCompat.getColor(activity!!, R.color.white))
        ViewCompat.setElevation(tpiShouye, 10f)
        tpiShouye.setupWithViewPager(vpShouye)

        popWindowHeight = (ThemeUtils.getStatusBarHeight(activity)
                - ThemeUtils.getStatusBarHeight(activity) - 45 - flShouye.measuredHeight)
        popWindowWidth = ThemeUtils.getWindowWidth(activity)
        dropDownAnim = AnimationUtils.loadAnimation(activity, R.anim.drop_down)
        dropUpAnim = AnimationUtils.loadAnimation(activity, R.anim.drop_up)
        return view
    }

    override fun load(): LoadingPage.LoadResult {
        return LoadingPage.LoadResult.success
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.ibAdd -> if (popupWindow == null) {
                if (vv == null)
                    vv = View.inflate(activity, R.layout.layout_add, null)
                popupWindow = PopupWindow(view, popWindowWidth, popWindowHeight)
                vv!!.startAnimation(dropDownAnim)
                val location = IntArray(2)
                ibAdd.getLocationOnScreen(location)
                popupWindow!!.showAtLocation(ibAdd, Gravity.NO_GRAVITY, 0, location[1] + flShouye.measuredHeight)
            } else {
                vv!!.startAnimation(dropUpAnim)
                Handler().postDelayed({
                    popupWindow!!.dismiss()
                    popupWindow = null
                }, 500)
            }
        }
    }
}
