package com.jw.dailyNews.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.jw.dailyNews.utils.ImageViewUtil;

/**
 * Created by liyuan on 16/12/6.
 */

public class ImageViewBinding {

    public static final int FLAG_NONE = 1<<0;
    public static final int FLAG_ROUND = 1 << 1;
    public static final int FLAG_ANIM = 1 << 2;
    public static final int FLAG_FIT_CENTER = 1 <<3;

    /**
     * 根据资源设置图片
     */
    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    /**
     * 异步加载图片, 使用默认策略
     */
    @BindingAdapter("uri")
    public static void setImageUri(ImageView iv, String oldRoundUri, String newRoundUri) {
        ImageViewUtil.setImageUriAsync(iv, newRoundUri);
    }

    @BindingAdapter({"uri", "default"})
    public static void setImageUri(ImageView iv, String oldRoundUri, Drawable oldRes,
                                   String newRoundUri, Drawable newRes) {
        if (TextUtils.isEmpty(newRoundUri)) {
            iv.setImageDrawable(newRes);
        } else {
            ImageViewUtil.setImageUriAsync(iv, newRoundUri);
        }
    }

    /**
     * 异步加载图片 圆形
     */
    @BindingAdapter({"round_uri"})
    public static void setImageRoundUriOld(ImageView iv, String oldRoundUri, String newRoundUri) {
        setImageRoundUri(iv, oldRoundUri, newRoundUri);
    }


    @BindingAdapter({"roundUri"})
    public static void setImageRoundUri(ImageView iv, String oldRoundUri, String newRoundUri) {
        ImageViewUtil.setImageUriRoundAsync(iv, newRoundUri);
    }

}
