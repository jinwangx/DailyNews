package com.jw.dailyNews.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jw.dailyNews.wiget.LoadingPage;

import java.util.List;

import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/3/25.
 */

public abstract class  BaseFragment extends Fragment {
    private LoadingPage loadingPage;
    protected Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("live","onCreateView");
        if(loadingPage==null){
            loadingPage=new LoadingPage(getActivity()) {
                @Override
                public View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }

                @Override
                protected LoadResult load() {
                    return BaseFragment.this.load();
                }
            };
            show();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。

        ViewGroup parent = (ViewGroup) loadingPage.getParent();
        if (parent != null) {
            parent.removeView(loadingPage);

        }
        return loadingPage;
    }



    /***
     *  创建成功的界面
     * @return
     */
    public  abstract View createSuccessView();
    /**
     * 请求服务器
     * @return
     */
    protected abstract LoadingPage.LoadResult load();

    public void show(){
        if(loadingPage!=null){
            loadingPage.show();
        }
    }


    /**校验数据 */
    public LoadingPage.LoadResult checkData(List datas) {
        if(datas==null){
            return LoadingPage.LoadResult.error;//  请求服务器失败
        }else{
            if(datas.size()==0){
                return LoadingPage.LoadResult.empty;
            }else{
                return LoadingPage.LoadResult.success;
            }
        }
    }

    @Override
    public void onDestroyView() {
        Log.v("live","onCreateView");
        super.onDestroyView();
        if(unbinder!=null)
            unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        Log.v("live","onAttach");
        super.onAttach(context);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.v("live","onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.v("live","onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }
}
