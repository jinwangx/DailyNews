package com.jw.dailyNews.activity

import Lib.NewsManager
import android.content.DialogInterface
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.sina.weibo.SinaWeibo
import com.baidu.location.*
import com.bumptech.glide.Glide
import com.jw.dailyNews.R
import com.jw.dailyNews.base.BaseActivity
import com.jw.dailyNews.fragment.FragmentDireBroad
import com.jw.dailyNews.fragment.FragmentMe
import com.jw.dailyNews.fragment.FragmentMe.Companion.rlMe
import com.jw.dailyNews.fragment.FragmentShouye
import com.jw.dailyNews.fragment.FragmentVideo
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.utils.CommonUtils
import com.jw.dailyNews.utils.NetUtils
import com.jw.dailyNews.utils.ThemeUtils
import com.jw.dailyNews.wiget.ColorPickDialog
import com.jw.dailyNews.wiget.ColorPickView
import com.jw.dailyNews.wiget.ElasticTouchListener
import com.jw.dailyNews.wiget.ItemSwitchView
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_home.*
import kotlinx.android.synthetic.main.layout_left.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.*


class HomeActivity : BaseActivity(),
        View.OnClickListener, RadioGroup.OnCheckedChangeListener,
        ColorPickView.OnColorChangedListener, ItemSwitchView.SwitchListener, NewsManager.AuthListener {

    //调用ShareSDK进行认证所需要平台的名称
    private val QQ = ShareSDK.getPlatform("")
    private val Sina = ShareSDK.getPlatform(SinaWeibo.NAME)
    private val WeChat = ShareSDK.getPlatform("")
    //activity要加载的四个fragment引用
    private lateinit var fragmentShouye: FragmentShouye
    private lateinit var fragmentVideo: FragmentVideo
    private lateinit var fragmentDireBroad: FragmentDireBroad
    private lateinit var fragmentMe: FragmentMe
    private lateinit var ft: FragmentManager
    private val fragments = ArrayList<Fragment>()
    private lateinit var toggle: ActionBarDrawerToggle
    private var mLocationClient: LocationClient? = null
    private var myListener: MyLocationListener? = null
//BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    /**
     * 窗帘拉开监听，重新加载缓存大小数据
     */
    private val drawerOpenListenernew = object : SimpleDrawerListener() {
        override fun onDrawerOpened(drawerView: View) {
            super.onDrawerOpened(drawerView)
            itvClearCache.setText(CommonUtils.getCacheSize())
        }
    }

    /**
     * 清除缓存dialog确定按钮点击监听，监听到点击事件后重新加载缓存大小数据
     */
    private val dialogOkListener = DialogInterface.OnClickListener { dialog, which ->
        Thread {
            run {
                CommonUtils.clearCache()
                runOnUiThread {
                    itvClearCache!!.setText(CommonUtils.getCacheSize())
                }
            }
        }.start()
    }


    override fun bindView() {
        setContentView(R.layout.activity_home)
    }

    override fun initView() {
        inittb()
        initRadioButton()
    }

    override fun initEvent() {
        super.initEvent()
        llLeftPanel.setOnTouchListener(ElasticTouchListener())
        civLoginQQ.setOnClickListener(this)
        civLoginWechat.setOnClickListener(this)
        civLoginWeibo.setOnClickListener(this)
        itvStyle.setOnClickListener(this)
        itvClearCache.setOnClickListener(this)
        civMeLeft.setOnClickListener(this)
        tvNameLeft.setOnClickListener(this)
        iavFontSize.setOnClickListener(this)
        isvVideoAutoPlay.setSwitchListener(this, ORDER_AUTO_PLAY)
        isv4gRemind.setSwitchListener(this, ORDER_4G_REMIND)
        isvHeadline.setSwitchListener(this, ORDER_HEAD_LINE)
        isvImageDownloadOnlyWifi.setSwitchListener(this, ORDER_DOWNLOAD_ONLY_WIFI)
        initLeftPanel()
    }

    /**
     * 初始化左面板
     * 1.个人头像(所在授权成功平台对应的头像)
     * 2.调用Baidu地图Api获取当前位置信息
     * 3.从config读取switchButton状态(有实际作用)
     */
    private fun initLeftPanel() {
        val type = NetUtils.isNetworkAvailable(this)
        if (type != "没联网")
            ThemeUtils.show(this, type)
        isvHeadline!!.setChecked(
                CacheUtils.getCacheBoolean("JPush", true))
        isvImageDownloadOnlyWifi!!.setChecked(
                CacheUtils.getCacheBoolean("isvImageDownloadOnlyWifi", true))
        itvStyle!!.setText(
                CacheUtils.getCacheInt("indicatorColor", Color.RED).toString() + "")
        mLocationClient = LocationClient(applicationContext);
        myListener=MyLocationListener()
        //声明LocationClient类
        mLocationClient!!.registerLocationListener(myListener)
        //注册监听函数
        val option = LocationClientOption()

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient!!.locOption = option
        mLocationClient!!.start()
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    /**
     * 初始化tb颜色，并与左面板关联,google提供的DrawerLayout,并且设置开启关闭监听
     */
    private fun inittb() {
        setSupportActionBar(tb)
        ThemeUtils.changeViewColor(tb, CacheUtils.getCacheInt("indicatorColor", Color.RED))
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        //tb上面最左边显示三杠图标监听DrawerLayout
        toggle = ActionBarDrawerToggle(
                this, dl, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        dl.addDrawerListener(toggle!!)
        toggle!!.syncState()
        dl.addDrawerListener(drawerOpenListenernew)
    }

    /**
     * 初始化radioGroup,用fragmentManager将四个fragment添加进fl_content
     */
    private fun initRadioButton() {
        fragmentShouye = FragmentShouye()
        fragmentVideo = FragmentVideo()
        fragmentDireBroad = FragmentDireBroad()
        fragmentMe = FragmentMe()
        ft = supportFragmentManager
        ft!!.beginTransaction().add(R.id.flHome, fragmentShouye, "新闻").add(R.id.flHome, fragmentVideo, "视频").add(R.id.flHome, fragmentDireBroad, "直播").add(R.id.flHome, fragmentMe, "我").commit()
        fragments.add(fragmentShouye)
        fragments.add(fragmentVideo)
        fragments.add(fragmentDireBroad)
        fragments.add(fragmentMe)
        switchContent(fragmentShouye)
        rbGroup.setOnCheckedChangeListener(this)
    }

    /**
     * activity切换对应fragment
     * @param to 要显示的fragment
     */
    fun switchContent(to: Fragment?) {
        val transaction = ft!!.beginTransaction()
        for (fragment in fragments) {
            if (fragment !== to) {
                transaction.hide(fragment)
            } else {
                if (to !== fragmentShouye) {
                    dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)  //禁止手势滑动
                    toggle!!.isDrawerIndicatorEnabled = false
                } else {
                    dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)   //打开手势滑动
                    toggle!!.isDrawerIndicatorEnabled = true
                }
                transaction.show(fragment)
                tbTitle.text = to.tag
            }
        }
        transaction.commit()
    }


    override fun onClick(v: View) {
        when (v.id) {
        //R.id.civLoginQQ -> ThemeUtils.show(this, "QQ登录暂未开放")
        //R.id.civLoginWechat -> NewsManager.Companion.get().auth(WeChat, this)
            R.id.civLoginWeibo -> NewsManager.get().auth(Sina, this)
            R.id.iavFontSize -> {
            }
            R.id.itvClearCache -> {
                val builder = AlertDialog.Builder(this@HomeActivity)
                builder.setMessage("确定删除所有缓存？")
                builder.setPositiveButton("确定", dialogOkListener)
                builder.setNegativeButton("取消") { dialog, which -> }
                builder.show()
            }
            R.id.itvStyle -> {
                val dialog = ColorPickDialog(this@HomeActivity)
                dialog.show()
                dialog.setOnColorChangedListener(this)
            }
        }
    }

    /**
     * radioButton点击监听，显示对应fragment,隐藏其他fragment
     * @param group
     * @param checkedId
     */
    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.rbShouye -> switchContent(fragmentShouye)
            R.id.rbVideo -> switchContent(fragmentVideo)
            R.id.rbDirbroad -> switchContent(fragmentDireBroad)
            R.id.rbMe -> switchContent(fragmentMe)
        }
    }

    /**
     * 主题颜色更改监听
     * @param color color
     */
    override fun onColorChange(color: Int) {
        CacheUtils.setCache("indicatorColor", color)
        ThemeUtils.changeStatusBar(this@HomeActivity,
                CacheUtils.getCacheInt("indicatorColor", Color.RED))
        ThemeUtils.changeViewColor(tb,
                CacheUtils.getCacheInt("indicatorColor", Color.RED))
        ThemeUtils.changeViewColor(rlMe,
                CacheUtils.getCacheInt("indicatorColor", Color.RED))
        itvStyle!!.setText(color.toString() + "")
    }

    /**
     * SwitchButton打开监听
     * @param order order
     */
    override fun onOpen(order: Int) {
        when (order) {
            ORDER_AUTO_PLAY -> isvVideoAutoPlay!!.setChecked(false)
            ORDER_4G_REMIND -> {
            }
            ORDER_HEAD_LINE -> {
/*                if (JPushInterface.isPushStopped(application))
                    JPushInterface.resumePush(application)*/
                CacheUtils.setCache("JPush", true)
            }
            ORDER_DOWNLOAD_ONLY_WIFI -> CacheUtils.setCache("isvImageDownloadOnlyWifi", true)
        }//ThemeUtils.show(this,"该功能暂未实现,敬请等候");
        //ThemeUtils.show(this,"4G网络下观看视频，该功能不管开启或未开启，都会提醒");
        //ThemeUtils.show(this,"已允许接收推送");
        //ThemeUtils.show(this,"非WIFI网络状态下将不加载图片");
    }

    /**
     * SwitchButton关闭监听
     * @param order order
     */
    override fun onClose(order: Int) {
        when (order) {
            ORDER_AUTO_PLAY -> {
            }
            ORDER_4G_REMIND -> {
            }
            ORDER_HEAD_LINE -> {
/*                if (!JPushInterface.isPushStopped(application))
                    JPushInterface.stopPush(application)*/
                CacheUtils.setCache("JPush", false)
            }
            ORDER_DOWNLOAD_ONLY_WIFI -> CacheUtils.setCache("isvImageDownloadOnlyWifi", false)
        }
    }

    /**
     * 授权成功监听
     * @param platform 授权成功的平台
     */
    override fun onAuthSuccess(platform: Platform) {
        runOnUiThread {
            val platformName = platform.name
            if (platformName == "QQ") {
                Toast.makeText(this@HomeActivity, "QQ登录成功", Toast.LENGTH_SHORT).show()
                tvNameLeft!!.text = QQ.db.userName
                Glide.with(this@HomeActivity).load(
                        QQ.db.userIcon).into(civMeLeft!!)
            } else if (platformName == "WeChat") {
                Toast.makeText(this@HomeActivity, "微信登录成功", Toast.LENGTH_SHORT).show()
                tvNameLeft!!.text = WeChat.db.userName
                Glide.with(this@HomeActivity).load(
                        WeChat.db.userIcon).into(civMeLeft!!)
            } else if (platformName == "SinaWeibo") {
                Toast.makeText(this@HomeActivity, "新浪登录成功", Toast.LENGTH_SHORT).show()
                tvNameLeft!!.text = Sina.db.userName
                //mBinding.name=WeChat.db.userName
                Glide.with(this@HomeActivity).load(
                        Sina.db.userIcon).into(civMeLeft!!)
            } else {

            }
            Toast.makeText(this@HomeActivity,
                    "已授权,过期时间:" + platform.db.expiresTime, Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onPause() {
        super.onPause()
        JCVideoPlayer.releaseAllVideos()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            JCVideoPlayer.backPress()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            val addr = location.addrStr    //获取详细地址信息
            val country = location.country    //获取国家
            val province = location.province    //获取省份
            val city = location.city    //获取城市
            val district = location.district    //获取区县
            val street = location.street    //获取街道信息
            tvLocationLeft.text = addr
        }
    }

    companion object {
        //左面板四个switchButton的order
        private val ORDER_AUTO_PLAY = 0
        private val ORDER_4G_REMIND = 1
        private val ORDER_HEAD_LINE = 2
        private val ORDER_DOWNLOAD_ONLY_WIFI = 3
    }

}
