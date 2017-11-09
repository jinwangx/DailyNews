package Lib;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jw.dailyNews.BaseApplication;
import com.jw.dailyNews.domain.NewsObject;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;

import Lib.callback.ObjectCallBack;
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
 * 描述：应用管理类，这里主要用于网络管理
 */

public class NewsManager {
    private static NewsManager instance;
    private OkHttpClient client;
    private Context context;

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
     * 同步请求，首次加载数据时
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
     * 异步请求,主要用于加载更多数据
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
                CacheUtils.setCache(url,result,context);
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
}
