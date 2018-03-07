package com.jw.dailyNews.bean

import java.util.*

/**
 * 创建时间：2017/7/12
 * 更新时间 2017/10/30 15:49
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

class NewsObject {

    var news: ArrayList<News>? = null
    var focus: ArrayList<News>? = null
    var list: ArrayList<News>? = null


    inner class News {
        var category: String?=null
        var digest: String?=null

        var imgsrc3gtype: Int = 0
        var link: String?=null
        var picInfo: ArrayList<PicInfo>?=null
        var ptime: String?=null
        var source: String?=null
        var tag: String?=null
        var tcount: Int = 0
        var title: String?=null
        var type: String?=null

        inner class PicInfo {

            var height: String?=null
            var ref: String?=null
            var url: String?=null
            var width: String?=null
        }
    }

}
