package Lib;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jw.dailyNews.BaseApplication;
import com.jw.dailyNews.bean.NewsObject;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Lib.callback.ObjectCallBack;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建时间：
 * 更新时间 2017/10/30 15:08
 * 版本：
 * 作者：Mr.jin
 * 描述：网络管理类,平台认证以及分享等相关调用
 */

public class NewsManager {
    private static NewsManager instance;
    private OkHttpClient client;
    private Context context;

    private ShowUserListener mInfoListener;
    private AuthListener mAuthListener;

    public static NewsManager getInstance(){
        synchronized (NewsManager.class) {
            if(instance==null)
                instance=new NewsManager();
        }
        return instance;
    }

    private NewsManager() {
        this.context= MyNews.getInstance().getContext();
        client = ((BaseApplication)context).getOkHttpClient();
    }

    /**
     * 平台认证
     * @param platform
     * @param listener
     */
    public void auth(Platform platform,AuthListener listener){
        this.mAuthListener=listener;
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                mAuthListener.onAuthSuccess(platform);
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
            mAuthListener.onAuthSuccess(platform);
        }
    }

    /**
     * 退出平台认证
     * @param platform
     */
    public void exitAuth(Platform platform){
        platform.removeAccount(true);
    }

    /**
     * 获取授权平台用户信息
     * @param platform
     * @param listener
     */
    public void showUser(Platform platform,ShowUserListener listener){
        this.mInfoListener =listener;
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> userInfos) {
                mInfoListener.onShowUserSuccess(userInfos);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(MyNews.getInstance().getContext(), "错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        platform.showUser(null);
    }

    /**
     * 调用分享
     * @param title
     * @param img
     * @param share_url
     */
    public void showShare(String title,String img,String share_url) {
        OnekeyShare oks = new OnekeyShare();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(share_url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("http://www.mob.com"+"\n"+"哈哈，太搞笑了");
        if(img!=null)
            oks.setImageUrl(img);

        // 启动分享GUI
        oks.show(MyNews.getInstance().getContext());
    }


    /**
     * 同步请求，首次加载页面新闻数据时
     * @param url
     * @param type
     * @return
     */
    public String doGet(String url,String type) {
        String result = "";
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                String str = response.body().string();
                result = CommonUtils.correctJson(type, str);
            }
        } catch (IOException e) {

        }
        return result;
    }

    /**
     * 异步请求,用于请求更多新闻数据(加载更多数据时)
     * @param url
     * @param type
     * @param callBack
     */
    public void doGetAsy(final String url, final String type, final ObjectCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                String result = CommonUtils.correctJson(type, str);
                CacheUtils.setCache(url,result);
                Log.v("json",result);
                JsonParser parser=new JsonParser();
                JsonObject jsonArray = parser.parse(result).getAsJsonObject();
                Gson gson = new Gson();
                // 将 json 转化 成 List泛型
                TypeToken<NewsObject> token=new TypeToken<NewsObject>(){};
                NewsObject newsObject =gson.fromJson(jsonArray,token.getType());
                ArrayList<NewsObject.News> list= newsObject.getList();
                callBack.onSuccess(list);
            }
        });
    }

    public boolean isValid(Platform platform){
        return platform.getDb().isValid();
    }

    public interface ShowUserListener {
        void onShowUserSuccess(HashMap<String,Object> userInfos);
    }

    public interface AuthListener{
        void onAuthSuccess(Platform platform);
    }
}
