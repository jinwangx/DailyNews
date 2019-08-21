package Lib

import android.annotation.SuppressLint
import android.content.Context

/**
 * 创建时间：2017/6/16
 * 更新时间 2017/10/30 15:06
 * 版本：
 * 作者：Mr.jin
 * 描述：获取应用上下文类(静态内部类单例推荐)
 */

class MyNews private constructor() {
    lateinit var context: Context

    companion object {
        fun get() = MyNews.Inner.anotherSingle
    }

    fun init(context: Context) {
        this.context = context
    }

    private object Inner {
        @SuppressLint("StaticFieldLeak")
        var anotherSingle = MyNews()
    }
}
