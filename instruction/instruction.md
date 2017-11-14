# DailyNews
DailyNews是一款模仿网易新闻客户端的移动端Android应用，新闻抓取于网页，数据可实时刷新

### 用到的主要技术及封装的框架
1. 前端与移动端的交互，在新闻详情页面，由于网页源码太多冗余，通过Jsoup解析，将拿到的div.article与本地html
    模板重组，同时加载本地css样式、js,并屏蔽网页图片点击事件，通过本地js传递事件到本地响应图片点击事件。
2. 正则表达式的的大量应用，由于新闻列表数据以及新闻文章网页均来自于fiddler抓取，就算同种Item类型的数据也
    存在目标url或者html结构差异，这里根据自己查找到的规律，运用正则表达式对url进行修正或过滤。
2. 实现了新闻列表数据三级缓存，图片缓存机制由Glide自行控制，通过自定义GlideModule设置
    缓存路径、最大缓存等，所有缓存数据可由封装缓存工具类CacheUtils一键清除
3. loadingPage View框架（可根据页面加载数据状态切换不同视图，如loading、error、success、empty。）
4. ImageLoadingPage View框架(和loadingPage异曲同工，只不过修改了其loading视图，添加了图片加载进度显示)
5. Glide封装工具类GlideUtils可在不同网络状态下根据配置文件自行选择加载或者不加载图片

### 用到的控件及其拓展类、自定义View（README.md中都有gif图展示）:
1. ToolBar添加标题并居中，并与DrawerLayout关联，可随着应用主题的更改，和TabPageIndicator、系统状态栏同步更改颜色
2. RecycleView经拓展,能够添加多个header、footer，并具有上拉加载更多功能，还能够响应Item点击事件
   基于低耦合的理念，添加多个header、footer的功能在其默认adapter修饰类中实现；
                     上拉加载更多的功能在RefreshLayout的拓展类中实现(泛型)；
                     点击事件在adapter具体类中实现。
3. 多种形式的自定义View、dialog、组合控件。
4. style中AppTheme中activity进出动画以及其他属性主题的定制


### 用到的开源框架
* Glide
* ViewPagerIndicator
* PhotoView
* gif-drawable
* Butterknife
* JcPlayer

### 集成的第三方开放平台
1. ShareSDK
2. Jpush极光推送
3. baidu地图定位API