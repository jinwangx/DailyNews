package com.jw.dailyNews.bean

/**
 * 创建时间：2017/7/18
 * 更新时间 2017/10/30 15:49
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

class NewsNormal {
    var title: String? = null

    var imgextra: List<ImageSrc>? = null
    var source: String? = null
    var imgsrc: String? = null
    var digest: String? = null
    var commentCount: Int = 0
    var ptime: String? = null
    var url: String? = null
    var imgsrc3gtype: String? = null

    var skipURL: String? = null


    inner class ImageSrc {

        var imgsrc: String? = null
    }
}
