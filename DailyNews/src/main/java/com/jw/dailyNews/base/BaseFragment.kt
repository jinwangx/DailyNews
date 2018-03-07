package com.jw.dailyNews.base


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jw.dailyNews.wiget.LoadingPage

/**
 * Created by Administrator on 2017/3/25.
 */

abstract class BaseFragment : Fragment() {
    private var loadingPage: LoadingPage? = null

    fun findViewById(id:Int):View? {
        return view?.findViewById(id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.v("live", "onCreateView")
        if (loadingPage == null) {
            loadingPage = object : LoadingPage(activity) {
                override fun createSuccessView(): View {
                    return this@BaseFragment.createSuccessView()
                }

                override fun load(): LoadingPage.LoadResult {
                    return this@BaseFragment.load()
                }
            }
            show()
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。

        val parent = loadingPage?.parent?:null
        if(parent!=null)
            (parent as ViewGroup).removeView(loadingPage)
        return loadingPage
    }


    /***
     * 创建成功的界面
     * @return
     */
    abstract fun createSuccessView(): View

    /**
     * 请求服务器
     * @return
     */
    protected abstract fun load(): LoadingPage.LoadResult

    fun show() {
        if (loadingPage != null) {
            loadingPage!!.show()
        }
    }


    /**校验数据  */
    fun checkData(datas: List<*>?): LoadingPage.LoadResult {
        return if (datas == null) {
            LoadingPage.LoadResult.error//  请求服务器失败
        } else {
            if (datas.size == 0) {
                LoadingPage.LoadResult.empty
            } else {
                LoadingPage.LoadResult.success
            }
        }
    }

    override fun onDestroyView() {
        Log.v("live", "onCreateView")
        super.onDestroyView()
    }

    override fun onAttach(context: Context?) {
        Log.v("live", "onAttach")
        super.onAttach(context)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.v("live", "onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v("live", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

}
