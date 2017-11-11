package com.jw.dailyNews.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jw.dailyNews.R;
import com.jw.dailyNews.adapter.ViewPagerAdapter;
import com.jw.dailyNews.base.BaseActivity;
import com.jw.dailyNews.utils.CommonUtils;
import com.jw.dailyNews.utils.ThemeUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Lib.ThreadManager;
import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 创建时间：2017/8/20
 * 更新时间：2017/11/11 0011 下午 4:11
 * 作者：Mr.jin
 * 描述：
 */
public class ImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_pager)
    ViewPager imagePager;
    @BindView(R.id.tv_currentItem)
    TextView tvCurrentItem;
    @BindView(R.id.divide)
    TextView divide;
    @BindView(R.id.totalItem)
    TextView tvTotalItem;
    @BindView(R.id.tv_node)
    TextView tvNode;
    @BindView(R.id.tv_image_title)
    TextView tvImageTitle;
    private ViewPagerAdapter adapter;
    private Document document = null;
    private List<HashMap<String, String>> maps=new ArrayList<HashMap<String, String>>() {};

    @Override
    protected void bindView() {
        setContentView(R.layout.layout_image_circle);
    }


    @Override
    protected void initView() {
        super.initView();
        String url= CommonUtils.getArticalUrl(
                getIntent().getStringExtra("docurl"),"photoview");
        initToolBar();
        initViewPager(url);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.action_btn_back_selector);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 初始化图片viewpager
     * @param url
     */
    private void initViewPager(final String url) {
        imagePager.addOnPageChangeListener(this);
        ThreadManager.getInstance().createLongPool(3, 3, 2l).execute(new Runnable() {
            @Override
            public void run() {
                //jsoup解析，耗时操作在子线程中执行
                maps = getImages(url);
                Log.v("size",maps.size()+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<View> views = new ArrayList<>();
                        for (int i = 0; i < maps.size(); i++) {
                            final HashMap<String, String> map = maps.get(i);
                            PhotoView iv = new PhotoView(ImageActivity.this);
                            PhotoViewAttacher mAttacher = new PhotoViewAttacher(iv);
                            mAttacher.update();
                            mAttacher.onGlobalLayout();
                            iv.setAdjustViewBounds(true);
                            Glide.with(ImageActivity.this).load(map.get("image")).into(iv);
                            views.add(iv);
                            adapter = new ViewPagerAdapter(views);
                        }
                        imagePager.setAdapter(adapter);
                        tvImageTitle.setText(document.title().replace("_手机网易网",""));
                        tvTotalItem.setText(maps.size() + "");
                        tvNode.setText("          "+ maps.get(0).get("node"));
                    }
                });
            }
        });
    }


    /**
     * 用Jsoup从图片新闻网页中抓取图片信息，存入map集合中,再将map添加进List中
     * @param url 图片新闻网页链接
     * @return
     */
    private List<HashMap<String,String>> getImages(String url){
        try {
            document = Jsoup.connect(url).maxBodySize(0).get();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements select = document.select("div").select("main").select("section")
                .select("ul").select("li").select("div.img-wrap");
        List<HashMap<String, String>> maps = new ArrayList<>();
        for (int i = 0; i < select.size(); i++) {
            HashMap map = new HashMap();
            String img = select.get(i).select("img[data-src]").attr("data-src");
            map.put("image", img);

            String node = select.get(i).select("span.note").text();
            map.put("node", node);
            maps.add(map);
        }
        return maps;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvCurrentItem.setText(position + 1 + "");
        tvNode.setText("          "+maps.get(position).get("node"));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtils.changeStatusBar(this,Color.BLACK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
