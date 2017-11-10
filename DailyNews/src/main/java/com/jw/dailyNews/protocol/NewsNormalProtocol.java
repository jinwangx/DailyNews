package com.jw.dailyNews.protocol;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jw.dailyNews.bean.NewsNormal;

import java.util.List;

import Lib.NewsURL;

/**
 * 创建时间：2017/7/18
 * 更新时间：2017/11/11 0011 上午 12:21
 * 作者：Mr.jin
 * 描述：继承了BaseProtocol,并且增加了将json数据解析成普通新闻bean对象集合的功能
 */
public class NewsNormalProtocol extends BaseProtocol<List<NewsNormal>> {
    public NewsNormalProtocol(String baseUrl, String url, Context context, String type) {
        super(baseUrl,url, context, type);
    }

    @Override
    public List<NewsNormal> parseJson(String json) {
        Log.v("json",json);
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        JsonArray jsonArray = root.get(NewsURL.getTag(baseUrl)).getAsJsonArray();
        // 将 json 转化 成 List泛型
        TypeToken<List<NewsNormal>> token=new TypeToken<List<NewsNormal>>(){};
        List<NewsNormal> newsNormalList =new Gson().fromJson(jsonArray,token.getType());
        return newsNormalList;
    }
}
