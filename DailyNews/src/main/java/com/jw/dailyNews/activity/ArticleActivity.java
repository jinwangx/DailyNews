package com.jw.dailyNews.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jw.dailyNews.R;
import com.jw.dailyNews.base.BaseActivity;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.CommonUtils;
import com.jw.dailyNews.utils.StreamUtils;
import com.jw.dailyNews.utils.ThemeUtils;
import com.jw.dailyNews.wiget.ImageLargeDialog;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import butterknife.ButterKnife;
import static com.jw.dailyNews.R.id.toolbar;
import static com.jw.dailyNews.R.id.webView;

/**
 * 创建时间：
 * 更新时间 2017/10/27 18:33
 * 版本：
 * 作者：Mr.jin
 * 描述：文章显示页面，将从网页拿到的html进行解析，得到article，然后与本地模板进行重组
 *       通过加载本地js代码,单独开启一个activity，使用photoView来显示网页中的图片
 */

public class ArticleActivity extends BaseActivity{

    private WebView mWebView;
    private ProgressBar mProgressbar;
    private Toolbar mToolbar;
    private String mUrl;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mUrl=getIntent().getStringExtra("docurl");
        Log.v("mUrl",mUrl);
        mUrl= CommonUtils.getArticalUrl(mUrl,"article");

        initAppBar();//初始化Toolbar
        initWebView();//初始化WebView
        initWebSettings();//初始化WebSettings
        initWebViewClient();//初始化WebViewClient
        initWebChromeClient();//初始化WebChromeClient
    }

    private void initAppBar() {
        mToolbar = (Toolbar) findViewById(toolbar);
        tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText("新闻详情");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        ThemeUtils.changeViewColor(mToolbar, CacheUtils.getCacheInt("indicatorColor", Color.RED,this));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.action_btn_back_selector);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果按下的是回退键且历史记录里确实还有页面
                if (mWebView.canGoBack())
                    mWebView.goBack();
                else
                    finish();
            }
        });
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(webView);
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void initWebSettings() {
        WebSettings settings = mWebView.getSettings();
        //支持获取手势焦点
        mWebView.requestFocusFromTouch();
        // 添加js交互接口类，并起别名 imagelistner
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        mWebView.addJavascriptInterface(new JavascriptInterface(this), "imageListener");
        //支持插件
        settings.setPluginState(WebSettings.PluginState.ON);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持缩放
        settings.setSupportZoom(true);
        //隐藏原生的缩放控件
        settings.setDisplayZoomControls(true);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.supportMultipleWindows();
        settings.setSupportMultipleWindows(true);
        //设置缓存模式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(mWebView.getContext().getCacheDir().getAbsolutePath());

        //设置可访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setNeedInitialFocus(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
    }


    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(final String img) {
            Log.v("imagg",img);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageLargeDialog dialog=new ImageLargeDialog(ArticleActivity.this,img);
                    dialog.show();
                }
            });
        }
    }

    private void initWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {

            //页面开始加载时
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressbar.setVisibility(View.VISIBLE);
            }

            String result = null;
            //页面完成加载时
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressbar.setVisibility(View.GONE);
                InputStream js = null;
                try {
                    js = getAssets().open("js.txt");
                    result = StreamUtils.readfromStream(js);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // html加载完成之后，添加监听图片的点击js函数
                mWebView.loadUrl(result);
            }

            //是否在WebView内加载新页面
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!url.contains("imageView")&&!url.contains("jpg")&&
                        !url.contains("jpeg"))
                    view.loadUrl(url);
                return true;
            }

            //网络错误时回调的方法
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                /**
                 * 在这里写网络错误时的逻辑,比如显示一个错误页面
                 *
                 * 这里我偷个懒不写了
                 * */
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url)
            {
                if(mUrl.equals(url)) {
                    return new WebResourceResponse("text/html", "utf-8",
                            new ByteArrayInputStream(CommonUtils.getArticleHtml(url).getBytes()));
                }else if(url.equals("https://main.css/")){
                    return
                            new WebResourceResponse("text/css","utf-8", StreamUtils.getAssetsInputStream("main.css"));
                } else
                    return null;
            }
        });
        mWebView.loadUrl(mUrl);
    }


    private void initWebChromeClient() {

        mWebView.setWebChromeClient(new WebChromeClient() {

            private Bitmap mDefaultVideoPoster;//默认的视频展示图

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                if (mDefaultVideoPoster == null) {
                    mDefaultVideoPoster = BitmapFactory.decodeResource(
                            getResources(), R.drawable.launcher
                    );
                    return mDefaultVideoPoster;
                }
                return super.getDefaultVideoPoster();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //如果按下的是回退键且历史记录里确实还有页面
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
