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

import butterknife.ButterKnife;

/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:52
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class FragmentShouye extends BaseFragment {

    ImageButton add;
    private PopupWindow popupWindow;

    @Override
    public View createSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_shouye, null);
        final FrameLayout fl= (FrameLayout) view.findViewById(R.id.fl);
        add= (ImageButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            View view;
            @Override
            public void onClick(View v) {
                if(popupWindow==null) {
                    int height = getActivity().getWindowManager().getDefaultDisplay()
                            .getHeight()- ThemeUtils.getStatusBarHeight(getActivity())-45-fl.getMeasuredHeight();
                    int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                    view = View.inflate(getActivity(), R.layout.layout_add, null);
                    popupWindow = new PopupWindow(view, width, height);
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.drop_down);
                    view.startAnimation(animation);
                    int[] location = new int[2];
                    add.getLocationOnScreen(location);
                    popupWindow.showAtLocation(add, Gravity.NO_GRAVITY, 0, location[1]+fl.getMeasuredHeight());
                }
                else {
                    Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.drop_up);
                    view.startAnimation(animation2);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                            popupWindow=null;
                        }
                    },500);
                }

            }
        });
        unbinder=ButterKnife.bind(this, view);
        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        TabPageIndicatorAdapter adapter = new TabPageIndicatorAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        return view;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        return LoadingPage.LoadResult.success;
    }
}
