package com.jw.dailyNews.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
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
import com.jw.dailyNews.utils.MyGlideModule;
import com.jw.dailyNews.utils.NetUtils;
import com.jw.dailyNews.utils.ThemeUtils;
import com.jw.dailyNews.utils.ToastUtils;
import com.jw.dailyNews.wiget.ColorPickDialog;
import com.jw.dailyNews.wiget.ColorPickView;
import com.jw.dailyNews.wiget.ElasticTouchListener;
import com.jw.dailyNews.wiget.ItemArrowView;
import com.jw.dailyNews.wiget.ItemSwitchView;
import com.jw.dailyNews.wiget.ItemTextView;

import java.util.ArrayList;
import java.util.List;

import Lib.AuthManager;
import Lib.ThreadManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.jw.dailyNews.fragment.FragmentMe.rlMe;


public class HomeActivity extends BaseActivity implements View.OnClickListener {
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
    private Platform QQ = ShareSDK.getPlatform(cn.sharesdk.tencent.qq.QQ.NAME);
    private Platform Sina = ShareSDK.getPlatform(SinaWeibo.NAME);
    private Platform WeChat = ShareSDK.getPlatform(Wechat.NAME);
    private FragmentShouye fragmentShouye;
    private FragmentVideo fragmentVideo;
    private FragmentDireBroad fragmentDireBroad;
    private FragmentMe fragmentMe;
    private FragmentManager ft;
    private List<Fragment> fragments = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    private  LocationClient mLocationClient = null;

    public BDLocationListener myListener = new MyLocationListener();
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
        initToolBar();
        initRadioButton();
        initListener();
        initLeftPanel();
    }

    private void initLeftPanel() {
        String available = NetUtils.isNetworkAvailable(this);
        ToastUtils.show(this,available);
        isvHeadline.setChecked(CacheUtils.getCacheBoolean("JPush",true,HomeActivity.this));
        isvImageDownloadOnlyWifi.setChecked(CacheUtils.getCacheBoolean("isvImageDownloadOnlyWifi", true, HomeActivity.this));
        itvStyle.setText(CacheUtils.getCacheInt("indicatorColor",Color.RED, this)+"");

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    private void initListener() {
        llLeftMenu.setOnTouchListener(new ElasticTouchListener());
        loginQq.setOnClickListener(this);
        loginWechat.setOnClickListener(this);
        loginWeibo.setOnClickListener(this);
        itvStyle.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        userName.setOnClickListener(this);
        iavFontSize.setOnClickListener(this);

        isvVideoAutoPlay.setSwitchListener(new ItemSwitchView.SwitchListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }
        });
        isv4gRemind.setSwitchListener(new ItemSwitchView.SwitchListener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onClose() {
            }
        });
        isvHeadline.setSwitchListener(new ItemSwitchView.SwitchListener() {
            @Override
            public void onOpen() {
                if(JPushInterface.isPushStopped(getApplication()))
                    JPushInterface.resumePush(getApplication());
                CacheUtils.setCache("JPush",true,HomeActivity.this);
            }

            @Override
            public void onClose() {
                if(!JPushInterface.isPushStopped(getApplication()))
                    JPushInterface.stopPush(getApplication());
                CacheUtils.setCache("JPush",false,HomeActivity.this);
            }
        });
        isvImageDownloadOnlyWifi.setSwitchListener(new ItemSwitchView.SwitchListener() {
            @Override
            public void onOpen() {
                CacheUtils.setCache("isvImageDownloadOnlyWifi", true, HomeActivity.this);
            }

            @Override
            public void onClose() {
                CacheUtils.setCache("isvImageDownloadOnlyWifi", false, HomeActivity.this);
            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        ThemeUtils.changeViewColor(toolbar, CacheUtils.getCacheInt("indicatorColor",Color.RED, this));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Toolbar上面最左边显示三杠图标监听DrawerLayout
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                clearCache.setText(CacheUtils.getCacheSize(HomeActivity.this, null, MyGlideModule.getCacheDirectory()) + "MB");
            }
        });
    }

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

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
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
        });
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_qq:
                AuthManager.getInstance().auth(QQ, new AuthManager.AuthListener() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "QQ登录成功", Toast.LENGTH_SHORT).show();
                                userName.setText(QQ.getDb().getUserName());
                                Glide.with(HomeActivity.this).load(QQ.getDb().getUserIcon()).into(userIcon);
                            }
                        });
                    }
                });
                break;
            case R.id.login_wechat:
                AuthManager.getInstance().auth(WeChat, new AuthManager.AuthListener() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "微信登录成功", Toast.LENGTH_SHORT).show();
                                userName.setText(WeChat.getDb().getUserName());
                                Glide.with(HomeActivity.this).load(WeChat.getDb().getUserIcon()).into(userIcon);
                            }
                        });
                    }
                });
                break;
            case R.id.login_weibo:
                AuthManager.getInstance().auth(Sina, new AuthManager.AuthListener() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HomeActivity.this, "新浪登录成功", Toast.LENGTH_SHORT).show();
                                userName.setText(Sina.getDb().getUserName());
                                Glide.with(HomeActivity.this).load(Sina.getDb().getUserIcon()).into(userIcon);
                            }
                        });
                    }
                });
                break;
            case R.id.iav_fontSize:

                break;
            case R.id.itv_clear_cache:
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("确定删除所有缓存？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThreadManager.getInstance().createLongPool(3, 3, 2l).execute(new Runnable() {
                            @Override
                            public void run() {
                                CacheUtils.clear(HomeActivity.this);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        clearCache.setText(CacheUtils.getCacheSize(HomeActivity.this, null, MyGlideModule.getCacheDirectory()) + "MB");
                                    }
                                });
                            }
                        });
                    }
                });
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
                dialog.setOnColorChangedListener(new ColorPickView.OnColorChangedListener() {
                    @Override
                    public void onColorChange(int color) {
                        CacheUtils.setCache("indicatorColor", color, HomeActivity.this);
                        ThemeUtils.changeStatusBar(HomeActivity.this, CacheUtils.getCacheInt("indicatorColor", Color.RED, HomeActivity.this));
                        ThemeUtils.changeViewColor(toolbar, CacheUtils.getCacheInt("indicatorColor", Color.RED, HomeActivity.this));
                        ThemeUtils.changeViewColor(rlMe, CacheUtils.getCacheInt("indicatorColor", Color.RED, HomeActivity.this));
                        itvStyle.setText(color+"");
                    }
                });
                break;
        }
    }

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

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            mLocationClient.stop();
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

}
