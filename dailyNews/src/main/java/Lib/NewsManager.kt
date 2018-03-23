package Lib

import Lib.callback.ObjectCallBack
import android.content.Context
import android.util.Log
import android.widget.Toast
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.onekeyshare.OnekeyShare
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.jw.dailyNews.BaseApplication
import com.jw.dailyNews.R
import com.jw.dailyNews.bean.NewsObject
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.utils.CommonUtils
import okhttp3.*
import java.io.IOException
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/10/30 15:08
 * 版本：
 * 作者：Mr.jin
 * 描述：网络管理类,平台认证以及分享等相关调用
 */

class NewsManager private constructor() {
    private val client: OkHttpClient
    private val context: Context

    private var mInfoListener: ShowUserListener? = null
    private var mAuthListener: AuthListener? = null

    init {
        this.context = MyNews.get().context
        client = BaseApplication.okHttpClient
    }

    /**
     * 平台认证
     * @param platform
     * @param listener
     */
    fun auth(platform: Platform, listener: AuthListener) {
        this.mAuthListener = listener
        platform.platformActionListener = object : PlatformActionListener {
            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
                mAuthListener!!.onAuthSuccess(platform)
            }

            override fun onError(platform: Platform, i: Int, throwable: Throwable) {

            }

            override fun onCancel(platform: Platform, i: Int) {

            }
        }
        if (!isValid(platform))
            platform.authorize()
        else {
            mAuthListener!!.onAuthSuccess(platform)
        }
    }

    /**
     * 退出平台认证
     * @param platform
     */
    fun exitAuth(platform: Platform) {
        platform.removeAccount(true)
    }

    /**
     * 获取授权平台用户信息
     * @param platform
     * @param listener
     */
    fun showUser(platform: Platform, listener: ShowUserListener) {
        this.mInfoListener = listener
        platform.platformActionListener = object : PlatformActionListener {
            override fun onComplete(platform: Platform, i: Int, userInfos: HashMap<String, Any>) {
                mInfoListener!!.onShowUserSuccess(userInfos)
            }

            override fun onError(platform: Platform, i: Int, throwable: Throwable) {
                Toast.makeText(MyNews.get().context, "错误", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel(platform: Platform, i: Int) {

            }
        }
        platform.showUser(null)
    }

    /**
     * 调用分享
     * @param title
     * @param img
     * @param share_url
     */
    fun showShare(title: String, img: String, share_url: String) {

        val oks = OnekeyShare()
        //关闭sso授权
        oks.disableSSOWhenAuthorize()

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title)
        oks.setTitleUrl(share_url)
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.text = "哈哈，真是太搞笑了"
        oks.setImageUrl(img)
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(share_url)
        //oks.setUrl(share_url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我的评论:");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name))
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn")
        // 启动分享GUI
        oks.show(context)
    }


    /**
     * 同步请求，首次加载页面新闻数据时
     * @param url
     * @param type
     * @return
     */
    fun doGet(url: String, type: String): String {
        var result = ""
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (response.code() == 200) {
                val str = response.body()!!.string()
                result = CommonUtils.correctJson(type, str)
            }
        } catch (e: IOException) {

        }

        return result
    }

    /**
     * 异步请求,用于请求更多新闻数据(加载更多数据时)
     * @param url
     * @param type
     * @param callBack
     */
    fun doGetAsy(url: String, type: String, callBack: ObjectCallBack<*>) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result = CommonUtils.correctJson(type, str)
                CacheUtils.setCache(url, result)
                Log.v("json", result)
                val parser = JsonParser()
                val jsonArray = parser.parse(result).asJsonObject
                val gson = Gson()
                // 将 json 转化 成 List泛型
                val token = object : TypeToken<NewsObject>() {

                }
                val newsObject = gson.fromJson<NewsObject>(jsonArray, token.type)
                val list = newsObject.list
                //callBack.onSuccess(list)
            }
        })
    }

    fun isValid(platform: Platform): Boolean {
        return platform.db.isValid
    }

    interface ShowUserListener {
        fun onShowUserSuccess(userInfos: HashMap<String, Any>)
    }

    interface AuthListener {
        fun onAuthSuccess(platform: Platform)
    }

    companion object {
        fun get()= NewsManager.Inner.anotherSingle
    }

    private object Inner{
        var anotherSingle= NewsManager()
    }
}
