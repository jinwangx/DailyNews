package com.jw.dailyNews.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.jw.dailyNews.R;
import com.jw.dailyNews.base.BaseActivity;
import com.jw.dailyNews.fragment.FragmentDireBroad;
import com.jw.dailyNews.fragment.FragmentMe;
import com.jw.dailyNews.fragment.FragmentShouye;
import com.jw.dailyNews.fragment.FragmentVideo;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.CommonUtils;
import com.jw.dailyNews.utils.NetUtils;
import com.jw.dailyNews.utils.ThemeUtils;
import com.jw.dailyNews.wiget.ColorPickDialog;
import com.jw.dailyNews.wiget.ColorPickView;
import com.jw.dailyNews.wiget.ElasticTouchListener;
import com.jw.dailyNews.wiget.ItemArrowView;
import com.jw.dailyNews.wiget.ItemSwitchView;
import com.jw.dailyNews.wiget.ItemTextView;

import java.util.ArrayList;
import java.util.List;

import Lib.NewsManager;
import Lib.ThreadManager;
import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.jw.dailyNews.fragment.FragmentMe.rlMe;


public class HomeActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,ColorPickView.OnColorChangedListener,
        ItemSwitchView.SwitchListener, NewsManager.AuthListener {
    @BindView(R.id.user_icon)
    CircleImageView userIcon;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rb_group)
    RadioGroup rbGroup;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.isv_headline)
    ItemSwitchView isvHeadline;
    @BindView(R.id.isv_4gRemind)
    ItemSwitchView isv4gRemind;
    @BindView(R.id.isv_image_download_only_wifi)
    ItemSwitchView isvImageDownloadOnlyWifi;
    @BindView(R.id.isv_video_auto_play)
    ItemSwitchView isvVideoAutoPlay;
    @BindView(R.id.iav_fontSize)
    ItemArrowView iavFontSize;
    @BindView(R.id.itv_clear_cache)
    ItemTextView clearCache;
    @BindView(R.id.itv_style)
    ItemTextView itvStyle;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.login_qq)
    CircleImageView loginQq;
    @BindView(R.id.login_wechat)
    CircleImageView loginWechat;
    @BindView(R.id.login_weibo)
    CircleImageView loginWeibo;
    @BindView(R.id.ll_left_menu)
    LinearLayout llLeftMenu;
    @BindView(R.id.tv_location)
    TextView tvLocation;

    //调用ShareSDK进行认证所需要平台的名称
    private Platform QQ = ShareSDK.getPlatform(cn.sharesdk.tencent.qq.QQ.NAME);
    private Platform Sina = ShareSDK.getPlatform(SinaWeibo.NAME);
    private Platform WeChat = ShareSDK.getPlatform(Wechat.NAME);
    //activity要加载的四个fragment引用
    private FragmentShouye fragmentShouye;
    private FragmentVideo fragmentVideo;
    private FragmentDireBroad fragmentDireBroad;
    private FragmentMe fragmentMe;
    private FragmentManager ft;
    private List<Fragment> fragments = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    //baidu地图位置管理者
    private  LocationClient mLocationClient = null;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    //baidu地图位置监听
    public BDLocationListener myListener = new MyLocationListener();
    //左面板四个switchButton的order
    private final static int ORDER_AUTO_PLAY=0;
    private final static int ORDER_4G_REMIND=1;
    private final static int ORDER_HEAD_LINE=2;
    private final static int ORDER_DOWNLOAD_ONLY_WIFI =3;



    @Override
    protected void bindView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void initView() {
        initToolBar();
        initRadioButton();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        llLeftMenu.setOnTouchListener(new ElasticTouchListener());
        loginQq.setOnClickListener(this);
        loginWechat.setOnClickListener(this);
        loginWeibo.setOnClickListener(this);
        itvStyle.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        userName.setOnClickListener(this);
        iavFontSize.setOnClickListener(this);
        isvVideoAutoPlay.setSwitchListener(this,ORDER_AUTO_PLAY);
        isv4gRemind.setSwitchListener(this,ORDER_4G_REMIND);
        isvHeadline.setSwitchListener(this,ORDER_HEAD_LINE);
        isvImageDownloadOnlyWifi.setSwitchListener(this, ORDER_DOWNLOAD_ONLY_WIFI);
        initLeftPanel();
    }

    /**
     * 初始化左面板
     * 1.个人头像(所在授权成功平台对应的头像)
     * 2.调用Baidu地图Api获取当前位置信息
     * 3.从config读取switchButton状态(有实际作用)
     */
    private void initLeftPanel() {
        String type = NetUtils.isNetworkAvailable(this);
        if(!type.equals("没联网"))
            ThemeUtils.show(this,type);
        isvHeadline.setChecked(
                CacheUtils.getCacheBoolean("JPush",true));
        isvImageDownloadOnlyWifi.setChecked(
                CacheUtils.getCacheBoolean("isvImageDownloadOnlyWifi", true));
        itvStyle.setText(
                CacheUtils.getCacheInt("indicatorColor",Color.RED)+"");
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    /**
     * 初始化toolbar颜色，并与左面板关联,google提供的DrawerLayout,并且设置开启关闭监听
     */
    private void initToolBar() {
        setSupportActionBar(toolbar);
        ThemeUtils.changeViewColor(toolbar, CacheUtils.getCacheInt("indicatorColor",Color.RED));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Toolbar上面最左边显示三杠图标监听DrawerLayout
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.addDrawerListener(drawerOpenListenernew);
    }

    /**
     * 初始化radioGroup,用fragmentManager将四个fragment添加进fl_content
     */
    private void initRadioButton() {
        fragmentShouye = new FragmentShouye();
        fragmentVideo = new FragmentVideo();
        fragmentDireBroad = new FragmentDireBroad();
        fragmentMe = new FragmentMe();
        ft = getSupportFragmentManager();
        ft.beginTransaction().
                add(R.id.fl_content, fragmentShouye, "新闻").
                add(R.id.fl_content, fragmentVideo, "视频").
                add(R.id.fl_content, fragmentDireBroad, "直播").
                add(R.id.fl_content, fragmentMe, "我").commit();
        fragments.add(fragmentShouye);
        fragments.add(fragmentVideo);
        fragments.add(fragmentDireBroad);
        fragments.add(fragmentMe);
        switchContent(fragmentShouye);
        rbGroup.setOnCheckedChangeListener(this);
    }

    /**
     * activity切换对应fragment
     * @param to 要显示的fragment
     */
    public void switchContent(Fragment to) {
        FragmentTransaction transaction = ft.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != to) {
                transaction.hide(fragment);
            } else {
                if (to != fragmentShouye) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);  //禁止手势滑动
                    toggle.setDrawerIndicatorEnabled(false);
                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);   //打开手势滑动
                    toggle.setDrawerIndicatorEnabled(true);
                }
                transaction.show(fragment);
                toolbarTitle.setText(to.getTag());
            }
        }
        transaction.commit();
    }

    /**
     * 初始化百度地图位置服务配置
     */
    private void initLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        //option.setIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        //option.setWifiValidTime(5*60*1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_qq:
                NewsManager.getInstance().auth(QQ, this);
                break;
            case R.id.login_wechat:
                NewsManager.getInstance().auth(WeChat, this);
                break;
            case R.id.login_weibo:
                NewsManager.getInstance().auth(Sina,this);
                break;
            case R.id.iav_fontSize:

                break;
            case R.id.itv_clear_cache:
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("确定删除所有缓存？");
                builder.setPositiveButton("确定", dialogOkListener);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;
            case R.id.itv_style:
                ColorPickDialog dialog=new ColorPickDialog(HomeActivity.this);
                dialog.show();
                dialog.setOnColorChangedListener(this);
                break;
        }
    }

    /**
     * radioButton点击监听，显示对应fragment,隐藏其他fragment
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_shouye:
                switchContent(fragmentShouye);
                break;
            case R.id.rb_video:
                switchContent(fragmentVideo);
                break;
            case R.id.rb_dirbroad:
                switchContent(fragmentDireBroad);
                break;
            case R.id.rb_me:
                switchContent(fragmentMe);
                break;
        }
    }

    /**
     * 主题颜色更改监听
     * @param color color
     */
    @Override
    public void onColorChange(int color) {
        CacheUtils.setCache("indicatorColor", color);
        ThemeUtils.changeStatusBar(HomeActivity.this,
                CacheUtils.getCacheInt("indicatorColor", Color.RED));
        ThemeUtils.changeViewColor(toolbar,
                CacheUtils.getCacheInt("indicatorColor", Color.RED));
        ThemeUtils.changeViewColor(rlMe,
                CacheUtils.getCacheInt("indicatorColor", Color.RED));
        itvStyle.setText(color+"");
    }

    /**
     * SwitchButton打开监听
     * @param order order
     */
    @Override
    public void onOpen(int order) {
        switch (order){
            case ORDER_AUTO_PLAY:
                isvVideoAutoPlay.setChecked(false);
                //ThemeUtils.show(this,"该功能暂未实现,敬请等候");
                break;
            case ORDER_4G_REMIND:
                //ThemeUtils.show(this,"4G网络下观看视频，该功能不管开启或未开启，都会提醒");
                break;
            case ORDER_HEAD_LINE:
                if(JPushInterface.isPushStopped(getApplication()))
                    JPushInterface.resumePush(getApplication());
                CacheUtils.setCache("JPush",true);
                //ThemeUtils.show(this,"已允许接收推送");
                break;
            case ORDER_DOWNLOAD_ONLY_WIFI:
                CacheUtils.setCache("isvImageDownloadOnlyWifi", true);
                //ThemeUtils.show(this,"非WIFI网络状态下将不加载图片");
                break;
        }
    }

    /**
     * SwitchButton关闭监听
     * @param order order
     */
    @Override
    public void onClose(int order) {
        switch (order){
            case ORDER_AUTO_PLAY:

                break;
            case ORDER_4G_REMIND:

                break;
            case ORDER_HEAD_LINE:
                if(!JPushInterface.isPushStopped(getApplication()))
                    JPushInterface.stopPush(getApplication());
                CacheUtils.setCache("JPush",false);
                break;
            case ORDER_DOWNLOAD_ONLY_WIFI:
                CacheUtils.setCache("isvImageDownloadOnlyWifi", false);
                break;
        }
    }

    /**
     * 窗帘拉开监听，重新加载缓存大小数据
     */
    private SimpleDrawerListener drawerOpenListenernew=new SimpleDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            clearCache.setText(CommonUtils.getCacheSize());
        }
    };

    /**
     * 授权成功监听
     * @param platform 授权成功的平台
     */
    @Override
    public void onAuthSuccess(final Platform platform) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String platformName=platform.getName();
                if(platformName.equals("QQ")){
                    Toast.makeText(HomeActivity.this, "QQ登录成功", Toast.LENGTH_SHORT).show();
                    userName.setText(QQ.getDb().getUserName());
                    Glide.with(HomeActivity.this).load(
                            QQ.getDb().getUserIcon()).into(userIcon);
                }else if(platformName.equals("WeChat")){
                    Toast.makeText(HomeActivity.this, "微信登录成功", Toast.LENGTH_SHORT).show();
                    userName.setText(WeChat.getDb().getUserName());
                    Glide.with(HomeActivity.this).load(
                            WeChat.getDb().getUserIcon()).into(userIcon);
                }else if(platformName.equals("SinaWeibo")){
                    Toast.makeText(HomeActivity.this, "新浪登录成功", Toast.LENGTH_SHORT).show();
                    userName.setText(Sina.getDb().getUserName());
                    Glide.with(HomeActivity.this).load(
                            Sina.getDb().getUserIcon()).into(userIcon);
                }else {

                }
                Toast.makeText(HomeActivity.this,
                        "已授权,过期时间:" + platform.getDb().getExpiresTime(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 清除缓存dialog确定按钮点击监听，监听到点击事件后重新加载缓存大小数据
     */
    private DialogInterface.OnClickListener dialogOkListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ThreadManager.getInstance().createLongPool(
                    3, 3, 2l).execute(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.clearCache();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    clearCache.setText(CommonUtils.getCacheSize());
                                }
                            });
                        }
                    });
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            JCVideoPlayer.backPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            location.getTime();    //获取定位时间
            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
            location.getLocType();    //获取定位类型
            location.getLatitude();    //获取纬度信息
            location.getLongitude();    //获取经度信息
            location.getRadius();    //获取定位精准度
            location.getAddrStr();    //获取地址信息
            location.getCountry();    //获取国家信息
            location.getCountryCode();    //获取国家码
            location.getCity();    //获取城市信息
            location.getCityCode();    //获取城市码
            location.getDistrict();    //获取区县信息
            location.getStreet();    //获取街道信息
            location.getStreetNumber();    //获取街道码
            location.getLocationDescribe();    //获取当前位置描述信息
            location.getPoiList();    //获取当前位置周边POI信息

            location.getBuildingID();    //室内精准定位下，获取楼宇ID
            location.getBuildingName();    //室内精准定位下，获取楼宇名称
            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息

            String address=location.getCountry()+location.getCity()+location.getDistrict()
                    +location.getStreet()+location.getLocationDescribe();
            Log.v("address",address);
            if(address==null)
                return;
            mLocationClient.stop();
            tvLocation.setText(address);

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();    //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();    //获取当前卫星数
                location.getAltitude();    //获取海拔高度信息，单位米
                location.getDirection();    //获取方向信息，单位度

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                Toast.makeText(HomeActivity.this,location.getOperators(),Toast.LENGTH_SHORT).show();
                //当前为网络定位结果，可获取以下信息
                location.getOperators();    //获取运营商信息

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                Toast.makeText(HomeActivity.this,location.getCity(),Toast.LENGTH_SHORT).show();
                //当前为网络定位结果

            } else if (location.getLocType() == BDLocation.TypeServerError) {
                Toast.makeText(HomeActivity.this,"当前网络定位失败",Toast.LENGTH_SHORT).show();
                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Toast.makeText(HomeActivity.this,"当前网络不通",Toast.LENGTH_SHORT).show();
                //当前网络不通

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(HomeActivity.this,"当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限",Toast.LENGTH_SHORT).show();
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
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {

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

}
