package com.jw.dailyNews.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jw.dailyNews.R;
import com.jw.dailyNews.base.BaseActivity;
import com.jw.dailyNews.utils.ThemeUtils;

import Lib.ThreadManager;
import butterknife.BindView;

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 下午 9:11
 * 作者：Mr.jin
 * 描述：闪屏页面，translate动画完成后，检查存储卡写入权限是否开启，如未开启
 *       弹出权限框
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.rl_splash)
    RelativeLayout rlSplash;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        super.initView();
        //闪屏页面渐变动画
        Animation anim_splash_in = AnimationUtils.loadAnimation(this, R.anim.splash_in);
        final long startTime= System.currentTimeMillis();
        rlSplash.startAnimation(anim_splash_in);
        //固定停留本页面2s钟，2s钟后检查相关权限是否开启，如没开启，则弹出请求框请求用户开启
        ThreadManager.getInstance().createLongPool(3,3,2l).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long endTime= System.currentTimeMillis();
                    long time=endTime-startTime;
                    Thread.sleep(2000-time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //检查需要系统同意的请求是否开启
                    int hasPermission = ThemeUtils.checkPermission(
                            SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    //如果开启
                    if(hasPermission == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                        finish();
                    }
                    else {
                        //弹出请求框请求用户开启
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ThemeUtils.requestPermission(SplashActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }
                }
            }
        });
    }

    /**
     * 权限设置后的回调函数，判断相应设置
     * @param requestCode
     * @param permissions  requestPermissions传入的参数为几个权限
     * @param grantResults 对应权限的设置结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ThemeUtils.REQUEST_CODE_ASK_PERMISSIONS :
                //可以遍历每个权限设置情况
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    //这里写你需要相关权限的操作
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(SplashActivity.this,
                            "权限没有开启,将无法加载图片",Toast.LENGTH_SHORT).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }
}
