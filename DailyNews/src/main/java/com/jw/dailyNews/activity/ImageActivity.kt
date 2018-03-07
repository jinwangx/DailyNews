package com.jw.dailyNews.activity

import android.graphics.Color
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.View
import com.bumptech.glide.Glide
import com.jw.dailyNews.R
import com.jw.dailyNews.adapter.ViewPagerAdapter
import com.jw.dailyNews.base.BaseActivity
import com.jw.dailyNews.utils.CommonUtils
import com.jw.dailyNews.utils.ThemeUtils
import kotlinx.android.synthetic.main.layout_image_circle.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.IOException
import java.util.*

/**
 * 创建时间：2017/8/20
 * 更新时间：2017/11/11 0011 下午 4:11
 * 作者：Mr.jin
 * 描述：图片新闻详情页面，
 */
class ImageActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private var adapter: ViewPagerAdapter? = null
    private var document: Document? = null
    private var maps: List<HashMap<String, String>> = object : ArrayList<HashMap<String, String>>() {

    }

    override fun bindView() {
        setContentView(R.layout.layout_image_circle)
    }


    override fun initView() {
        super.initView()
        val url = CommonUtils.getArticalUrl(
                intent.getStringExtra("docurl"), "photoview")
        Log.v("url_image_article", url)
        initToolBar()
        initViewPager(url)
    }

    /**
     * 初始化toolbar
     */
    private fun initToolBar() {
        tb!!.setTitleTextColor(resources.getColor(android.R.color.white))
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tb!!.setNavigationIcon(R.drawable.action_btn_back_selector)
        tb!!.setNavigationOnClickListener { finish() }
        tb!!.setBackgroundColor(Color.TRANSPARENT)
    }

    /**
     * 初始化图片viewpager
     * @param url
     */
    private fun initViewPager(url: String) {
        vpImageArticle!!.addOnPageChangeListener(this)
        object : Thread() {
            override fun run() {
                super.run()
                //jsoup解析，耗时操作在子线程中执行
                maps = getImages(url)
                runOnUiThread {
                    val views = ArrayList<View>()
                    for (i in maps.indices) {
                        val map = maps[i]
                        val iv = PhotoView(this@ImageActivity)
                        val mAttacher = PhotoViewAttacher(iv)
                        mAttacher.update()
                        mAttacher.onGlobalLayout()
                        iv.adjustViewBounds = true
                        Glide.with(this@ImageActivity).load(map["image"]).into(iv)
                        views.add(iv)
                        adapter = ViewPagerAdapter(views)
                    }
                    vpImageArticle!!.adapter = adapter
                    tvImageTitle!!.text = document!!.title().replace("_手机网易网", "")
                    tvTotalItem!!.text = maps.size.toString() + ""
                    tvNode!!.text = "          " + maps[0]["node"]
                }
            }
        }.start()
    }


    /**
     * 用Jsoup从图片新闻网页中抓取图片信息，存入map集合中,再将map添加进List中
     * @param url 图片新闻网页链接
     * @return
     */
    private fun getImages(url: String): List<HashMap<String, String>> {
        try {
            document = Jsoup.connect(url).maxBodySize(0).get()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        val select = document!!.select("div").select("main").select("section")
                .select("ul").select("li").select("div.img-wrap")
        val maps = ArrayList<HashMap<String, String>>()
        for (i in select.indices) {
            val map = HashMap<String,String>()
            val img = select[i].select("img[data-src]").attr("data-src")
            map.put("image", img)

            val node = select[i].select("span.note").text()
            map.put("node", node)
            maps.add(map)
        }
        return maps
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        tvCurrentItem!!.text = (position + 1).toString() + ""
        tvNode!!.text = "          " + maps[position]["node"]
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.changeStatusBar(this, Color.BLACK)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
}
