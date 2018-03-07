package com.jw.dailyNews.bean

import java.util.*

/**
 * Created by Administrator on .
 */

/**
 * 创建时间：2017/7/20
 * 更新时间 2017/10/30 15:48
 * 版本：
 * 作者：Mr.jin
 * 描述：来自百思不得姐的json数据，封装为Joke类
 */

class Joke {
    var status: Int = 0
    var comment: String? = null        //评论
    var top_comments: ArrayList<TopComments>? = null

    var tags: List<Tags>? = null

    var image: Image? = null

    var gif: Gif? = null
    var bookmark: String? = null
    var text: String? = null          //标题
    var up: String? = null            //点赞

    var share_url: String? = null
    var down: Int = 0             //鄙视
    var forwrard: Int = 0         //转发
    var u: U? = null
    var passtime: String? = null      //发布时间
    var video: MVedio? = null
    var type: String? = null
    var id: String? = null

    inner class Tags

    inner class TopComments {
        var voicetime: Int = 0

        var status: Int = 0
        var hate_count: Int = 0
        var cmt_type: String? = null   //评论内容类型

        var precid: Int = 0
        var content: String? = null   //评论内容
        var like_count: Int = 0
        var u: U? = null
    }

    inner class U {

        var header: List<String>?=null       //发布者头像
        var uid: String? = null
        var is_vip: Boolean = false
        var is_v: Boolean = false
        var room_url: String? = null
        var room_name: String? = null
        var room_role: String? = null
        var room_icon: String? = null
        var name: String? = null      //发布者姓名
    }

    inner class MVedio {

        var playfcount: Int = 0
        var height: Int = 0
        var width: Int = 0
        var video: List<String>? = null
        var download: List<String>? = null
        var duration: Int = 0    //时长
        var playcount: Int = 0   //播放量
        var thumbnail: List<String>? = null
        var thumbnail_small: List<String>? = null
    }

    inner class Image {

        var medium: List<String>? = null
        var big: List<String>? = null
        var download_url: List<String>? = null
        var height: Int = 0
        var width: Int = 0
        var small: List<String>? = null
        var thumbnail_small: List<String>? = null

    }

    inner class Gif {
        var images: List<String>? = null
        var width: Int = 0
        var gif_thumbnail: List<String>? = null
        var download_url: List<String>? = null
        var height: Int = 0
    }
}
