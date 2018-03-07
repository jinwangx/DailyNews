package com.jw.dailyNews.protocol

import Lib.NewsManager
import Lib.callback.ObjectCallBack
import android.content.Context
import android.text.TextUtils
import com.jw.dailyNews.utils.CacheUtils

/**
 * 创建时间：2017/7/11
 * 更新时间：2017/11/11 0011 上午 12:18
 * 作者：Mr.jin
 * 描述：json数据三级缓存
 */
abstract class BaseProtocol<T>(var baseUrl: String, var mUrl: String, private val mContext: Context, private val type: String) {

    fun load(): T? {
        var json = loadLocal()
        if (TextUtils.isEmpty(json)) {
            json = loadServer()
            if (!TextUtils.isEmpty(json)) {
                saveLocal(json)
                return parseJson(json)
            } else
                return null
        } else
            return parseJson(json!!)
    }


    fun loadLocal(): String? {
        return CacheUtils.getCacheString(mUrl, null)
    }

    fun loadServer(): String {
        return NewsManager.Companion.get().doGet(mUrl, type)
    }

    fun loadServerAsy(callBack: ObjectCallBack<*>) {
        NewsManager.Companion.get().doGetAsy(mUrl, type, callBack)
    }

    private fun saveLocal(json: String) {
        CacheUtils.setCache(mUrl, json)
    }

    abstract fun parseJson(json: String): T

}
