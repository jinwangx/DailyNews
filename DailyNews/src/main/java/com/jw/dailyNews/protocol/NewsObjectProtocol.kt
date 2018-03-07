package com.jw.dailyNews.protocol

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.jw.dailyNews.bean.NewsObject
import java.util.*

/**
 * 创建时间：2017/7/12
 * 更新时间：2017/11/11 0011 上午 12:19
 * 作者：Mr.jin
 * 描述：继承了BaseProtocol,并且增加了将json数据解析成推荐新闻bean对象集合的功能
 */

class NewsObjectProtocol(baseUrl: String, url: String, context: Context?, type: String) : BaseProtocol<NewsObject>(baseUrl, url, context!!, type) {
    lateinit var newsList: ArrayList<NewsObject.News>
    var focus: ArrayList<NewsObject.News>? = null

    override fun parseJson(json: String): NewsObject {
        val parser = JsonParser()
        Log.v("json", json)
        val jsonArray = parser.parse(json).asJsonObject
        val gson = Gson()
        // 将 json 转化 成 List泛型
        val token = object : TypeToken<NewsObject>() {

        }
        val newsObject = gson.fromJson<NewsObject>(jsonArray, token.type)
        this.newsList = newsObject.list!!
        this.focus = newsObject.focus
        return newsObject
    }

    fun getFocus(): List<NewsObject.News>? {
        return focus
    }

}
