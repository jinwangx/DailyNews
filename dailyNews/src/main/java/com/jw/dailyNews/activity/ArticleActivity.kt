package com.jw.dailyNews.activity

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.webkit.*
import com.jw.dailyNews.R
import com.jw.dailyNews.base.BaseActivity
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.utils.CommonUtils
import com.jw.dailyNews.utils.ThemeUtils
import com.jw.dailyNews.wiget.ImageDetailDialog
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import android.icu.util.ULocale.getCountry
import com.baidu.location.BDLocation
import com.baidu.location.BDAbstractLocationListener



/**
 * 创建时间：
 * 更新时间 2017/10/27 18:33
 * 版本：
 * 作者：Mr.jin
 * 描述：文章显示页面，将从网页拿到的html进行解析，得到article，然后与本地模板进行重组
 * 通过加载本地js代码,单独开启一个activity，使用photoView来显示网页中的图片
 */

class ArticleActivity : BaseActivity(), View.OnClickListener {
    
    private lateinit var mUrl: String

    override fun bindView() {
        setContentView(R.layout.activity_detail)
    }

    override fun initView() {
        super.initView()
        mUrl = CommonUtils.getArticalUrl(intent.getStringExtra("docurl"), "article")
        Log.v("url_article", mUrl)
        initAppBar()//初始化Toolbar
        initWebView()//初始化WebView
        initWebSettings()//初始化WebSettings
        initWebViewClient()//初始化WebViewClient
        //initWebChromeClient()//初始化WebChromeClient
    }

    private fun initAppBar() {
        tbTitle.text = "新闻详情"
        tb.setTitleTextColor(resources.getColor(android.R.color.white))
        ThemeUtils.changeViewColor(tb, CacheUtils.getCacheInt("indicatorColor", Color.RED))
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tb.setNavigationIcon(R.drawable.action_btn_back_selector)
        tb.setNavigationOnClickListener(this)
    }

    private fun initWebView() {

    }

    private fun initWebSettings() {
        val settings = webView.settings
        //支持获取手势焦点
        webView.requestFocusFromTouch()
        // 添加js交互接口类，并起别名 imagelistner
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = false
        settings.pluginState = WebSettings.PluginState.ON
        webView.addJavascriptInterface(JavascriptInterface(this), "imageListener")
        //支持插件
        settings.pluginState = WebSettings.PluginState.ON
        //设置适应屏幕
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        //支持缩放
        settings.setSupportZoom(true)
        //隐藏原生的缩放控件
        settings.displayZoomControls = true
        //支持内容重新布局
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.supportMultipleWindows()
        settings.setSupportMultipleWindows(true)
        //设置缓存模式
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCacheEnabled(true)
        settings.setAppCachePath(webView.context.cacheDir.absolutePath)

        //设置可访问文件
        settings.allowFileAccess = true
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true)
        //支持自动加载图片
        settings.loadsImagesAutomatically = Build.VERSION.SDK_INT >= 19
        settings.setNeedInitialFocus(true)
        //设置编码格式
        settings.defaultTextEncodingName = "UTF-8"
    }

    // js通信接口
    inner class JavascriptInterface(private val context: Context) {

        @android.webkit.JavascriptInterface
        fun openImage(img: String) {
            Log.v("uri_article_img", img)
            runOnUiThread {
                val dialog = ImageDetailDialog(this@ArticleActivity, img)
                dialog.show()
            }
        }
    }

    private fun initWebViewClient() {
        webView.webViewClient = object : WebViewClient() {

            internal lateinit var result: String

            //页面开始加载时
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                pb.visibility = View.VISIBLE
            }


            //页面完成加载时
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                pb.visibility = View.GONE
                var js: InputStream
                try {
                    js = assets.open("js.txt")
                    result = ThemeUtils.readFromAssetsStream(js)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                // html加载完成之后，添加监听图片的点击js函数
                webView.loadUrl(result)
            }

            //是否在WebView内加载新页面
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //如果url不为图片链接,webView直接内部加载网页
                if (!url.contains("imageView") && !url.contains("jpg") &&
                        !url.contains("jpeg") && !url.contains("png") &&
                        !url.contains("bmp") && !url.contains("gif"))
                    view.loadUrl(url)
                Log.v("urlllllllll", url)
                return true
            }

            //网络错误时回调的方法
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                /**
                 * 在这里写网络错误时的逻辑,比如显示一个错误页面
                 *
                 * 这里我偷个懒不写了
                 */
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedHttpError(view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                return if (mUrl == url) {
                    WebResourceResponse("text/html", "utf-8",
                            ByteArrayInputStream(CommonUtils.getArticleHtml(url).toByteArray()))
                } else if (url == "https://main.css/") {
                    WebResourceResponse("text/css", "utf-8", ThemeUtils.getAssetsInputStream("main.css"))
                } else
                    null
            }
        }
        webView.loadUrl(mUrl)
    }


    private fun initWebChromeClient() {

        webView.webChromeClient = object : WebChromeClient() {

            private val mDefaultVideoPoster: Bitmap? = null//默认的视频展示图

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
            }

            override fun getDefaultVideoPoster(): Bitmap {
                /*                if (mDefaultVideoPoster == null) {
                    mDefaultVideoPoster = BitmapFactory.decodeResource(
                            getResources(), R.drawable.launcher
                    );
                    return mDefaultVideoPoster;
                }*/
                return super.getDefaultVideoPoster()
            }
        }
    }

    override fun onClick(v: View) {
        //如果按下的是回退键且历史记录里确实还有页面
        if (webView.canGoBack())
            webView.goBack()
        else
            finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        //如果按下的是回退键且历史记录里确实还有页面
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
}
