package com.jw.dailyNews.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jw.dailyNews.R;
import com.jw.dailyNews.base.BaseActivity;
import com.jw.dailyNews.utils.ThemeUtils;

import Lib.ThreadManager;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {

    @BindView(R.id.rl_splash)
    RelativeLayout rlSplash;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean handleMessage(Message msg) {
            ThemeUtils.checkPermission((Activity) SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE );
            while(true){
                int permission = ContextCompat.checkSelfPermission(
                        SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission==android.content.pm.PackageManager.PERMISSION_GRANTED )
                    break;
            }
            startActivity(new Intent(SplashActivity.this,HomeActivity.class));
            finish();
            return false;
        }
    });

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_splash);
        unbinder=ButterKnife.bind(this);
        initAnimation();
    }


    private void initAnimation(){
/*      AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator rotation=ObjectAnimator.ofFloat(rlSplash,"rotation", 0, 360);
        ObjectAnimator scaleX=ObjectAnimator.ofFloat(rlSplash,"scaleX", 0.1f,0.5f,0.1f,1);
        ObjectAnimator scaleY=ObjectAnimator.ofFloat(rlSplash,"scaleY", 0.1f,0.5f,0.1f,1);
        animatorSet.play(rotation).with(scaleX).with(scaleY);
        animatorSet.setDuration(2000);*/

        Animation anim_splash_in = AnimationUtils.loadAnimation(this, R.anim.splash_in);
        final long startTime= System.currentTimeMillis();
        rlSplash.startAnimation(anim_splash_in);
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
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }


    //权限设置后的回调函数，判断相应设置，requestPermissions传入的参数为几个权限，则permissions和grantResults为对应权限和设置结果
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ThemeUtils.REQUEST_CODE_ASK_PERMISSIONS :
                //可以遍历每个权限设置情况
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    //这里写你需要相关权限的操作
                }else{
                    Toast.makeText(SplashActivity.this,
                            "权限没有开启",Toast.LENGTH_SHORT).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }
}
