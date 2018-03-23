package com.jw.dailyNews.protocol;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jw.dailyNews.bean.Joke;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2017/7/20
 * 更新时间：2017/11/11 0011 上午 12:22
 * 作者：Mr.jin
 * 描述：继承了BaseProtocol,并且增加了将json数据解析成JokeBean对象集合的功能,而且可以将json数据分割
 *       成数段提供给页面加载，模拟页面加载更多的效果
 */
public class JokeProtocol extends BaseProtocol<List<Joke>> {

    public static List<Joke>[] lists;

    public JokeProtocol(String baseUrl, String url, Context context, String type) {
        super(baseUrl, url, context, type);
    }

    @Override
    public List<Joke> parseJson(String json) {
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        JsonArray jsonArray = root.get("list").getAsJsonArray();
        TypeToken<List<Joke>> typeToken = new TypeToken<List<Joke>>() {};
        List<Joke> list = new Gson().fromJson(jsonArray, typeToken.getType());
        devidePage(list);
        return list;
    }

    public List<Joke>[] devidePage(List<Joke> news){
        if((news.size()%10)==0)
            lists =new List[news.size()/10];
        else
            lists =new List[(news.size()/10)+1];
        for(int i = 0; i< lists.length; i++){
            if(i==(lists.length-1))
                lists[i] =  new ArrayList<>(news.subList(10*i, news.size()-1));
            else
                lists[i]= new ArrayList<>(news.subList(10*i,10*(i+1)-1));
        }
        return lists;
    }

    public  List<Joke> getList(int position){
        if(position<lists.length)
            return lists[position];
        else
            return null;
    }

    public  List<Joke>[] getList(){
        return lists;
    }
}
