package com.jw.dailyNews.fragment;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.jw.dailyNews.R;
import com.jw.dailyNews.adapter.TabPageIndicatorAdapter;
import com.jw.dailyNews.base.BaseFragment;
import com.jw.dailyNews.utils.ThemeUtils;
import com.jw.dailyNews.wiget.LoadingPage;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:52
 * 版本：
 * 作者：Mr.jin
 * 描述：主界面的首页界面，其中内嵌多个新闻fragment，默认页面为推荐新闻页面,NewsTabObject,
 *       其他页面类型均为NewsTabNormal
 */

public class FragmentShouye extends BaseFragment implements View.OnClickListener{

    private ImageButton add;
    private PopupWindow popupWindow;
    private View view;
    private FrameLayout fl;
    private int popWindowHeight;
    private int popWindowWidth;
    private Animation dropDownAnim;
    private Animation dropUpAnim;

    @Override
    public View createSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_shouye, null);
        fl = (FrameLayout) view.findViewById(R.id.fl);
        add= (ImageButton) view.findViewById(R.id.add);
        add.setOnClickListener(this);
        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        TabPageIndicatorAdapter adapter = new TabPageIndicatorAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        popWindowHeight = ThemeUtils.getStatusBarHeight(getActivity())
                - ThemeUtils.getStatusBarHeight(getActivity())-45-fl.getMeasuredHeight();
        popWindowWidth = ThemeUtils.getWindowWidth(getActivity());
        dropDownAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.drop_down);
        dropUpAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.drop_up);
        return view;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        return LoadingPage.LoadResult.success;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add:
                if(popupWindow==null) {
                    if(view==null)
                        view = View.inflate(getActivity(), R.layout.layout_add, null);
                    popupWindow = new PopupWindow(view, popWindowWidth, popWindowHeight);
                    view.startAnimation(dropDownAnim);
                    int[] location = new int[2];
                    add.getLocationOnScreen(location);
                    popupWindow.showAtLocation(add, Gravity.NO_GRAVITY, 0, location[1]+fl.getMeasuredHeight());
                }
                else {
                    view.startAnimation(dropUpAnim);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                            popupWindow=null;
                        }
                    },500);
                }
                break;
        }
    }
}
