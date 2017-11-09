package Lib;

import android.os.Handler;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * 创建时间：2017/7/26
 * 更新时间 2017/10/30 15:06
 * 版本：
 * 作者：Mr.jin
 * 描述：授权登陆管理类
 */

public class AuthManager {
    private static AuthManager mAuthMagager;
    private Platform weibo;
    private UserInfoListener mListener;
    private AuthListener mAuthListener;

    private AuthManager(){
        weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
    }
    public static AuthManager getInstance(){
        synchronized (AuthManager.class){
            if(mAuthMagager==null)
                mAuthMagager=new AuthManager();
            return mAuthMagager;
        }
    }

    public void auth(Platform platform,final AuthListener listener){
        this.mAuthListener=listener;
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess();
                    }
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        if(!isValid(platform))
            platform.authorize();
        else {
            Toast.makeText(MyNews.getInstance().getContext(), "已授权,过期时间:" + platform.getDb().getExpiresTime(), Toast.LENGTH_SHORT).show();
            listener.onSuccess();
        }
    }


    public void showUser(UserInfoListener listener){
        this.mListener=listener;
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> userInfos) {
                mListener.onSuccess(userInfos);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(MyNews.getInstance().getContext(), "错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        weibo.showUser(null);
    }

    public boolean isValid(Platform platform){
        return platform.getDb().isValid();
    }

    public void exitAutoSina(){
        weibo.removeAccount(true);
    }

    public void showShare(String title,String img,String share_url) {
        OnekeyShare oks = new OnekeyShare();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(share_url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("http://www.mob.com"+"\n"+"哈哈，太搞笑了");
        //oks.setImageUrl(img);

        // 启动分享GUI
        oks.show(MyNews.getInstance().getContext());
    }

    public interface UserInfoListener{
        void onSuccess(HashMap<String,Object> userInfos);
    }

    public interface AuthListener{
        void onSuccess();
    }
}
