package com.jw.dailyNews

import Lib.MyNews
import com.jw.dailyNews.utils.CacheUtils

/**
 * 创建时间：2017/11/11 0011 下午 5:42
 * 更新时间：2017/11/11 0011 下午 5:42
 * 作者：Mr.jin
 * 描述：通用常量
 */

interface Constants {
    companion object {
        val JSON_CACHE_PATH = MyNews.Companion.get().context.filesDir.parent + "/shared_prefs/" + CacheUtils.PREF_NAME + ".xml"
        val IMAGE_CACHE_PATH = MyGlideModule.cacheDirectory
        val PREF_NAME = "config"
    }
}
