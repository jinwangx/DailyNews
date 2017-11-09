package com.jw.dailyNews.fragment;

import android.view.View;

import com.jw.dailyNews.R;
import com.jw.dailyNews.base.BaseFragment;
import com.jw.dailyNews.wiget.LoadingPage;

/**
 * 创建时间：2017/6/21
 * 更新时间 2017/10/30 15:51
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class FragmentDireBroad extends BaseFragment{

    @Override
    public View createSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_direct_broad, null);
        return view;
    }

    @Override
    protected LoadingPage.LoadResult load() {
        return LoadingPage.LoadResult.success;
    }
}
