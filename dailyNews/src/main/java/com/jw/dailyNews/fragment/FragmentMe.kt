package com.jw.dailyNews.fragment

import Lib.NewsManager
import android.app.AlertDialog
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.sina.weibo.SinaWeibo
import com.bumptech.glide.Glide
import com.jw.dailyNews.R
import com.jw.dailyNews.base.BaseFragment
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.utils.ThemeUtils
import com.jw.dailyNews.wiget.ElasticTouchListener
import com.jw.dailyNews.wiget.LoadingPage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:52
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

class FragmentMe : BaseFragment(), View.OnClickListener, NewsManager.AuthListener, NewsManager.ShowUserListener {

    lateinit var tvMName: TextView
    lateinit var tvMDesc: TextView
    lateinit var tvMWeibo: TextView
    lateinit var tvMFans: TextView
    lateinit var tvMConcerns: TextView

    private lateinit var ivMe: CircleImageView
    private lateinit var dialog: AlertDialog
    private lateinit var userInfos: HashMap<String, Any>
    private val Sina = ShareSDK.getPlatform(SinaWeibo.NAME)

    private val mHandler = Handler(Handler.Callback {
        tvMName!!.text = userInfos!!["name"] as String
        tvMDesc!!.text = userInfos!!["description"] as String
        tvMWeibo!!.text = userInfos!!["credit_score"].toString() + ""
        tvMFans!!.text = userInfos!!["followers_count"].toString() + ""
        tvMConcerns!!.text = userInfos!!["friends_count"].toString() + ""
        Glide.with(context).load(userInfos!!["profile_image_url"]).into(ivMe!!)
        false
    })


    override fun onClick(v: View) {
        when (v.id) {
            R.id.civMef -> if (NewsManager.Companion.get().isValid(Sina))
                showExitDialog()
            else {
                NewsManager.Companion.get().auth(Sina, this)
            }
            R.id.tvCancelExit -> dialog!!.dismiss()
            R.id.tvOkExit -> {
                dialog!!.dismiss()
                NewsManager.Companion.get().exitAuth(Sina)
                ivMe!!.setImageResource(R.drawable.ic_default_user)
                tvMName!!.text = ""
                tvMDesc!!.text = ""
                tvMWeibo!!.text = ""
                tvMFans!!.text = ""
                tvMConcerns!!.text = ""
            }
        }
    }


    override fun load(): LoadingPage.LoadResult {
        return LoadingPage.LoadResult.success
    }

    override fun createSuccessView(): View {
        val view = View.inflate(activity, R.layout.fragment_me, null)
        ivMe = view.findViewById(R.id.civMef)
        rlMe = view.findViewById(R.id.rlMef)
        rlMe.setOnTouchListener(object : ElasticTouchListener() {})
        ivMe!!.setOnClickListener(this)
        ThemeUtils.changeViewColor(rlMe, CacheUtils.getCacheInt("indicatorColor", Color.RED))
        updateUserInfos()
        return view
    }

    /**
     * 更新用户资料
     */
    private fun updateUserInfos() {
        if (NewsManager.Companion.get().isValid(Sina)) {
            //ThemeUtils.show(getActivity(),"正在获取用户信息");
            NewsManager.Companion.get().showUser(Sina, this)
        }
    }


    /**
     * 退出登录dialog
     */
    private fun showExitDialog() {
        val builder = AlertDialog.Builder(context)
        //自定义对话框
        dialog = builder.create()
        val view = View.inflate(context, R.layout.dialog_login, null)
        dialog!!.setView(view)
        val cancelExit = view.findViewById<TextView>(R.id.tvCancelExit)
        val okExit = view.findViewById<TextView>(R.id.tvOkExit)
        cancelExit.setOnClickListener(this)
        okExit.setOnClickListener(this)
        dialog!!.show()
    }

    override fun onAuthSuccess(platform: Platform) {
        //ThemeUtils.show(getActivity(),"授权成功");
        updateUserInfos()
    }

    override fun onShowUserSuccess(userInfos: HashMap<String, Any>) {
        this.userInfos = userInfos
        //ThemeUtils.show(getActivity(),"获取用户信息成功");
        mHandler.sendEmptyMessage(0)
    }

    companion object {
        lateinit var rlMe: RelativeLayout
    }
}
