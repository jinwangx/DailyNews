package com.jw.dailyNews.protocol;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jw.dailyNews.domain.NewsObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class NewsObjectProtocol extends BaseProtocol<NewsObject> {
    private ArrayList<NewsObject.News> list;
    private ArrayList<NewsObject.News> focus;

    public NewsObjectProtocol(String baseUrl, String url, Context context, String type) {
        super(baseUrl,url, context, type);
    }

    @Override
    public NewsObject parseJson(String json) {
        JsonParser parser=new JsonParser();
        Log.v("json",json);
        JsonObject jsonArray = parser.parse(json).getAsJsonObject();
        Gson gson = new Gson();
        // 将 json 转化 成 List泛型
        TypeToken<NewsObject> token=new TypeToken<NewsObject>(){};
        NewsObject newsObject =gson.fromJson(jsonArray,token.getType());
        this.list = newsObject.getList();
        this.focus= newsObject.getFocus();
        return newsObject;
    }

    public ArrayList<NewsObject.News> getNewsList(){
        return list;
    }

    public List<NewsObject.News> getFocus(){
        return focus;
    }

}
