package com.jw.dailyNews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jw.dailyNews.activity.ImageActivity;

import java.util.List;

/**
 * 创建时间：
 * 更新时间：2017/11/11 0011 上午 12:26
 * 作者：Mr.jin
 * 描述：新闻首页推荐新闻部分大图轮播部分adapter
 */
public class TopNewsAdapter extends PagerAdapter {

    private List<String> links;
    private List<String> descs;
    private List<String> urls;
    private Context context;

    public TopNewsAdapter(Context context,List<String> links, List<String> descs,List<String> urls) {
        this.links=links;
        this.descs=descs;
        this.urls=urls;
        this.context=context;
    }

    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.FIT_XY);// 基于控件大小填充图片
        Glide.with(context).load(links.get(position)).into(image); // 传递imagView对象和图片地址
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("docurl",urls.get(position));
                intent.setClass(context, ImageActivity.class);
                context.startActivity(intent);
            }
        });
        container.addView(image);
        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}