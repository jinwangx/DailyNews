package com.jw.dailyNews;

import com.jw.dailyNews.utils.CacheUtils;

import Lib.MyNews;

/**
 * 创建时间：2017/11/11 0011 下午 5:42
 * 更新时间：2017/11/11 0011 下午 5:42
 * 作者：Mr.jin
 * 描述：通用常量
 */

public interface Constants {
    String JSON_CACHE_PATH= MyNews.getInstance().getContext().
            getFilesDir().getParent()+"/shared_prefs/"+ CacheUtils.PREF_NAME+".xml";
    String IMAGE_CACHE_PATH= MyGlideModule.getCacheDirectory();
    String PREF_NAME="config";
}
