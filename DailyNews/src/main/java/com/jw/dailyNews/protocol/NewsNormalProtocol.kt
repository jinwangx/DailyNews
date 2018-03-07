package com.jw.dailyNews.protocol

import Lib.NewsURL
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.jw.dailyNews.bean.NewsNormal

/**
 * 创建时间：2017/7/18
 * 更新时间：2017/11/11 0011 上午 12:21
 * 作者：Mr.jin
 * 描述：继承了BaseProtocol,并且增加了将json数据解析成普通新闻bean对象集合的功能
 */
class NewsNormalProtocol(baseUrl: String, url: String, context: Context, type: String) : BaseProtocol<List<NewsNormal>>(baseUrl, url, context, type) {

    override fun parseJson(json: String): List<NewsNormal> {
        Log.v("json", json)
        val root = JsonParser().parse(json).asJsonObject
        val jsonArray = root.get(NewsURL.getTag(baseUrl)).asJsonArray
        // 将 json 转化 成 List泛型
        val token = object : TypeToken<List<NewsNormal>>() {

        }
        return Gson().fromJson<List<NewsNormal>>(jsonArray, token.type)
    }
}
