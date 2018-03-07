package com.jw.dailyNews.wiget;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jw.dailyNews.R;

/**
 * 创建时间：2017/7/10
 * 更新时间：2017/11/11 0011 上午 12:44
 * 作者：Mr.jin
 * 描述：fragment的一个框架，
 *       能够根据传入的数据自动显示加载成功、加载中、加载错误等页面
 */

public abstract class LoadingPage extends FrameLayout {

    public static final int STATE_UNKOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;
    public int state = STATE_UNKOWN;

    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View emptyView;// 空界面
    private View successView;// 加载成功的界面
    private Context mContext;

    private Handler mHandler=new Handler();

    public LoadingPage(Context context) {
        this(context,null);
    }

    public LoadingPage(Context context,AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init();
    }

    /**
     * 初始化三个加载界面，加载中的界面，错误界面，空的界面
     *
     */
    private void init() {
        loadingView = createLoadingView(); // 创建了加载中的界面
        this.addView(loadingView, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        errorView = createErrorView(); // 加载错误界面
        this.addView(errorView, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        emptyView = createEmptyView(); // 加载空的界面
        this.addView(emptyView, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
            if (successView == null) {
                successView = createSuccessView();
                this.addView(successView, new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            successView.setVisibility(View.VISIBLE);
        } else {
            if (successView != null) {
                successView.setVisibility(View.INVISIBLE);
            }
        }
    }


    /**
     * 创建成功的界面
     * @return
     */
    public abstract View createSuccessView();

    /**
     * 空的界面
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(mContext, R.layout.loadpage_empty,
                null);
        return view;
    }

    /**
     * 错误的界面
     * @return
     */
    private View createErrorView() {
        View view = View.inflate(mContext, R.layout.loadpage_error,
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
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(mContext, R.layout.loadingpage_loading, null);
        return view;
    }

    public enum LoadResult {
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
    public void show() {
        if (state == STATE_ERROR || state == STATE_EMPTY) {
            state = STATE_LOADING;
        }
        // 请求服务器 获取服务器上数据 进行判断
        // 请求服务器 返回一个结果
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(500);
                final LoadResult result = load();
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (result != null) {
                            state = result.getValue();
                            showPage(); // 状态改变了,重新判断当前应该显示哪个界面
                        }
                    }
                });
            }
        }.start();
        showPage();
    }

    /**
     * 请求服务器
     *
     * @return
     */
    protected abstract LoadResult load();
}
