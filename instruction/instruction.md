# DailyNews
DailyNews是一款模仿网易新闻客户端的移动端Android应用，新闻抓取于网页，数据可实时刷新

### 本应用用到的控件、框架以及集成的开放平台API:
* 1.运用了drawerLayout,recyleView、toolbar等控件
* 2.运用了开源的Glide、viewpagerindicator、photoView、gif-drawable、jcPlayer、butterknife、等框架
* 3.运用了Joup，这个是网页解析重组用到的
* 4.集成了多个开放平台API，ShareSDK、Jpush、baidu地图API


### 本应用结构说明:

* SplashActivity闪屏页面:</br>
   ##### 布局
    toolbar+FramLayout(progressBar+webView）</br>
   ##### 功能
      * translate动画
      * 释放资源
      * 如果系统版本在7.0以上，还会在动画结束后弹出权限请求dialog

* HomeActivity应用主界面： 
   ##### 布局
    [!image](/instruction/主界面结构.png)
   ##### 功能
      * 百度api定位
      * 第三方登录
      * 极光消息推送
      * 是否仅wifi加载图片
      * 视频静默播放
      * 新闻正文字号
      * 缓存清理
      * app主题更改
      * 新闻list
    
* ArticleActivity新闻详情页面:
   ###### 布局
     toolbar+FramLayout(progressBar+webView）</br>
   ##### 功能
      * 由于新闻文章原网页含广告等冗余，还要实现本地的一些功能。所以重写了WebViewClient中对网页加载过程中的监听：
      * 网页开始加载时，progressBar可见
      * 在shouldInterceptRequest()中，当请求的时整个新闻网页url的时候，拿到网页的html，运用Jsoup抓取html中的article,然后与本地资源目录Assets         中的article.html进行重组，并加载本地css样式 *
      * 当页面加载完成后，加载本地js，使点击网页中的图片时，屏蔽网页中的事件处理，调用本地java代码，将图片url调给本地处理，弹出一个覆盖含             photoView控件的图片详情dialog 
      
* ImageActivity图片新闻页面:
   ##### 布局
     类似于网易新闻大图新闻的布局</br>
   ##### 功能
     * 运用Jsoup进行各种抓取，获取到图片信息,将图片加载到viewpager中，但viewpager容器中放的是photoView
 

### 拓展的重要控件、框架：

   * loadingPage
   
          本应用各页面用到的fragment都是继承自BaseFragment,BaseFragment的onCreateView中返回的是一个loadingPage.
      loadingPage是封装的一个继承自FramLayout的View框架，是一个抽象类，loadingPage保证不会被重复创建，并且
      可以根据fragment请求数据的状态，自动加载loading、success、error、empty画面。
      

   * HeaderAndFooterAdapter
   
         继承自RecycleView默认Adaper，其构造方法传入RecycleView默认Adapter，其实就是一个Adaper装饰类，通过修饰Adaper,recycleView
     具有可添加多个header、footer功能
     

   * MyRefreshLayout
    
         继承自RecycleView的RefreshLayout，RefreshLayout本来就具有下拉刷新的功能，这里通过进行拓展，使recycleView具有上拉加载更多
     数据的功能。
     
     
 
