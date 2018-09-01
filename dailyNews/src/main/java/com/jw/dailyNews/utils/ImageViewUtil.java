package com.jw.dailyNews.utils;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jw.dailyNews.BaseApplication;
import com.jw.dailyNews.R;


/**
 Created by liyuan on 16/11/6.
 */

public class ImageViewUtil {
    public static final float DEFAULT_PRESSED_ALPHA = 0.5f;
    public static final Option OPTION_ANIM = createBuilder().setAnima(true).build();
    public static final Option OPTION_DEFAULT = createBuilder().build();
    public static final Option OPTION_ROUND = createBuilder().setRound(true).build();
    public static final Option OPTION_ROUND_HEAD_IMG = createBuilder()
            .setRound(true)
            .build();

    public static OptionBuilder createBuilder() {
        return new OptionBuilder();
    }

    public static void setImageUriAsync(@NonNull ImageView iv, String uri) {
        setImageUriAsync(iv, uri, OPTION_DEFAULT);
    }

    public static void setImageUriAsync(@NonNull ImageView iv, String uri, Option option) {
        GlideUtils.load(iv.getContext(),uri,iv);
    }



    /**
     设置圆形icon
     */
    public static void setImageUriRoundAsync(@NonNull ImageView iv, String uri) {
        setImageUriAsync(iv, uri, OPTION_ROUND);
    }

    public static class Option {
        private OptionBuilder builder;

        private Option(OptionBuilder builder) {
            this.builder = builder;
        }

        @DrawableRes
        public int getFailRes() {
            return builder.failRes;
        }

        @DrawableRes
        public int getLoadingRes() {
            return builder.loadingRes;
        }

        public boolean isAnima() {
            return builder.anima;
        }

        public boolean isRound() {
            return builder.round;
        }
    }

    public static class OptionBuilder {
        private boolean anima = false;
        private @DrawableRes
        int failRes = 0;
        private @DrawableRes
        int loadingRes = 0;
        private boolean round = false;

        public Option build() {
            return new Option(this);
        }

        public OptionBuilder setAnima(boolean anima) {
            this.anima = anima;
            return this;
        }

        public OptionBuilder setFailRes(int failRes) {
            this.failRes = failRes;
            return this;
        }

        public OptionBuilder setLoadingRes(int loadingRes) {
            this.loadingRes = loadingRes;
            return this;
        }

        public OptionBuilder setRound(boolean round) {
            this.round = round;
            return this;
        }
    }
}