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
import cn.jpush.android.api.JPushInterface
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.sina.weibo.SinaWeibo
import cn.sharesdk.wechat.friends.Wechat
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
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
    private val QQ = ShareSDK.getPlatform(cn.sharesdk.tencent.qq.QQ.NAME)
    private val Sina = ShareSDK.getPlatform(SinaWeibo.NAME)
    private val WeChat = ShareSDK.getPlatform(Wechat.NAME)
    //activity要加载的四个fragment引用
    private lateinit var fragmentShouye: FragmentShouye
    private lateinit var fragmentVideo: FragmentVideo
    private lateinit var fragmentDireBroad: FragmentDireBroad
    private lateinit var fragmentMe: FragmentMe
    private lateinit var ft: FragmentManager
    private val fragments = ArrayList<Fragment>()
    private lateinit var toggle: ActionBarDrawerToggle
    //baidu地图位置管理者
    private var mLocationClient: LocationClient? = null
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    //baidu地图位置监听
    var myListener: BDLocationListener = MyLocationListener()

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
        Thread{
                run {
                    CommonUtils.clearCache()
                    runOnUiThread {
                        itvClearCache!!.setText(CommonUtils.getCacheSize()) }
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
        mLocationClient = LocationClient(applicationContext)
        //声明LocationClient类
        mLocationClient!!.registerLocationListener(myListener)
        //注册监听函数
        initLocation()
        mLocationClient!!.start()
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

    /**
     * 初始化百度地图位置服务配置
     */
    private fun initLocation() {

        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll")
        //可选，默认gcj02，设置返回的定位结果坐标系

        val span = 1000
        option.setScanSpan(span)
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true)
        //可选，设置是否需要地址信息，默认不需要

        option.isOpenGps = true
        //可选，默认false,设置是否使用gps

        option.isLocationNotify = true
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false)
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        //option.setIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false)
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        //option.setWifiValidTime(5*60*1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient!!.locOption = option
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.civLoginQQ ->
                //NewsManager.getInstance().auth(QQ, this);
                ThemeUtils.show(this, "QQ登录暂未开放")
            R.id.civLoginWechat -> NewsManager.Companion.get().auth(WeChat, this)
            R.id.civLoginWeibo -> NewsManager.Companion.get().auth(Sina, this)
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
                if (JPushInterface.isPushStopped(application))
                    JPushInterface.resumePush(application)
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
                if (!JPushInterface.isPushStopped(application))
                    JPushInterface.stopPush(application)
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

    internal inner class MyLocationListener : BDLocationListener {

        override fun onReceiveLocation(location: BDLocation) {

            //获取定位结果
            location.time    //获取定位时间
            location.locationID    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.locType    //获取定位类型
            location.latitude    //获取纬度信息
            location.longitude    //获取经度信息
            location.radius    //获取定位精准度
            location.addrStr    //获取地址信息
            location.country    //获取国家信息
            location.countryCode    //获取国家码
            location.city    //获取城市信息
            location.cityCode    //获取城市码
            location.district    //获取区县信息
            location.street    //获取街道信息
            location.streetNumber    //获取街道码
            location.locationDescribe    //获取当前位置描述信息
            location.poiList    //获取当前位置周边POI信息

            location.buildingID    //室内精准定位下，获取楼宇ID
            location.buildingName    //室内精准定位下，获取楼宇名称
            location.floor    //室内精准定位下，获取当前位置所处的楼层信息

            val address = (location.country + location.city + location.district
                    + location.street + location.locationDescribe)
            Log.v("address", address)
            if (address == null)
                return
            mLocationClient!!.stop()
            tvLocationLeft.text = address

            if (location.locType == BDLocation.TypeGpsLocation) {

                //当前为GPS定位结果，可获取以下信息
                location.speed    //获取当前速度，单位：公里每小时
                location.satelliteNumber    //获取当前卫星数
                location.altitude    //获取海拔高度信息，单位米
                location.direction    //获取方向信息，单位度

            } else if (location.locType == BDLocation.TypeNetWorkLocation) {
                Toast.makeText(this@HomeActivity, location.operators, Toast.LENGTH_SHORT).show()
                //当前为网络定位结果，可获取以下信息
                location.operators    //获取运营商信息

            } else if (location.locType == BDLocation.TypeOffLineLocation) {
                Toast.makeText(this@HomeActivity, location.city, Toast.LENGTH_SHORT).show()
                //当前为网络定位结果

            } else if (location.locType == BDLocation.TypeServerError) {
                Toast.makeText(this@HomeActivity, "当前网络定位失败", Toast.LENGTH_SHORT).show()
                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com

            } else if (location.locType == BDLocation.TypeNetWorkException) {
                Toast.makeText(this@HomeActivity, "当前网络不通", Toast.LENGTH_SHORT).show()
                //当前网络不通

            } else if (location.locType == BDLocation.TypeCriteriaException) {
                Toast.makeText(this@HomeActivity, "当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限", Toast.LENGTH_SHORT).show()
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码

            }
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        fun onLocDiagnosticMessage(locType: Int, diagnosticType: Int, diagnosticMessage: String) {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {

                //建议打开GPS

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {

                //建议打开wifi，不必连接，这样有助于提高网络定位精度！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {

                //定位权限受限，建议提示用户授予APP定位权限！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {

                //网络异常造成定位失败，建议用户确认网络状态是否异常！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {

                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {

                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {

                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {

                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {

                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！

            }
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
