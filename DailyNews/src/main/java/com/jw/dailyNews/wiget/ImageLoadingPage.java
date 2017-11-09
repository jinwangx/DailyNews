package com.jw.dailyNews.wiget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jw.dailyNews.BaseApplication;
import com.jw.dailyNews.R;
import com.jw.dailyNews.utils.CacheUtils;
import com.jw.dailyNews.utils.NetUtils;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import pl.droidsonroids.gif.GifImageView;

import static com.jw.dailyNews.wiget.LoadingPage.STATE_UNKOWN;

/**
 * Author: Administrator
 * Created on:  2017/8/14.
 * Description:
 */

public class ImageLoadingPage extends FrameLayout {

    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_EMPTY = 3;
    private static final int STATE_SUCCESS = 4;
    private GifImageView ivSuccess;
    private RoundProgressBar roundProgressBar2;

    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View emptyView;// 空界面
    private View successView;// 加载成功的界面
    public int state = STATE_UNKOWN;

    private final Context mContext;
    private String mUrl;
    private TextView tvClickLocd;

    public ImageLoadingPage(@NonNull Context context) {
        this(context, null);
    }

    public ImageLoadingPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLoadingPage(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }


    private void initView() {
        loadingView = createLoadingView(mContext); // 创建了加载中的界面
        this.addView(loadingView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        errorView = createErrorView(mContext); // 加载错误界面
        this.addView(errorView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        emptyView = createEmptyView(mContext); // 加载空的界面
        this.addView(emptyView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        successView = createSuccessView(mContext);
        this.addView(successView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        ivSuccess= (GifImageView) successView.findViewById(R.id.iv_success);
        roundProgressBar2= (RoundProgressBar) loadingView.findViewById(R.id.roundProgressBar2);
        showPage();
    }

    public void setImageUrl(String url) {
        this.mUrl = url;
        show();
    }

    // 根据不同的状态显示不同的界面
    private void showPage() {
        loadingView.setVisibility(state == STATE_UNKOWN
                || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                : View.INVISIBLE);
        emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                : View.INVISIBLE);
        if (state == STATE_SUCCESS) {
            successView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 创建成功的界面
     *
     * @param context
     * @return
     */
    private View createSuccessView(Context context) {
        View view = View.inflate(context, R.layout.loadpage_success,
                null);
        return view;
    }

    /**
     * 空的界面
     *
     * @param context
     * @return
     */
    private View createEmptyView(Context context) {
        View view = View.inflate(context, R.layout.loadpage_empty,
                null);
        tvClickLocd = (TextView) view.findViewById(R.id.click_load);
        return view;
    }

    /**
     * 错误的界面
     *
     * @param context
     * @return
     */
    private View createErrorView(final Context context) {
        View view = View.inflate(context, R.layout.loadpage_error,
                null);
        Button page_bt = (Button) view.findViewById(R.id.page_bt);
        page_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                show();
            }
        });
        return view;
    }

    /**
     * 加载的界面
     *
     * @param context
     * @return
     */
    private View createLoadingView(Context context) {
        View view = View.inflate(context, R.layout.loadpage_loading, null);
        return view;
    }

    private enum LoadResult {
        error(2), empty(3), success(4);

        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


    // 根据服务器的数据 切换状态
    private void show() {
        if (state == STATE_ERROR || state == STATE_EMPTY) {
            state = STATE_LOADING;
        }
        if(NetUtils.isNetworkAvailable(mContext).equals("MOBILE")&& CacheUtils.getCacheBoolean("isvImageDownloadOnlyWifi",true,mContext)){
            tvClickLocd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    state=STATE_LOADING;
                    loadImage();
                }
            });
            state=STATE_EMPTY;
            showPage();
            return;
        }
        loadImage();
    }

    private void loadImage(){
        showPage();
        ProgressManager.getInstance().addResponseListener(mUrl, new ProgressListener() {
            @Override
            public void onProgress(final ProgressInfo progressInfo) {
                int percent=progressInfo.getPercent();
                roundProgressBar2.setProgress(percent);
                if(percent==100)
                    state = STATE_SUCCESS;
                showPage();

            }
            @Override
            public void onError(long id, Exception e) {
                state=STATE_ERROR;
                showPage();
            }
        });

        Log.v("imageUrl",mUrl);
        // 请求服务器 获取服务器上数据 进行判断
        // 请求服务器 返回一个结果
        Glide.with(mContext).load(mUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                state=STATE_ERROR;
                showPage();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).apply(BaseApplication.options).into(ivSuccess);
    }
}
