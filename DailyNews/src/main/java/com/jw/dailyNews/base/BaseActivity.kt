package com.jw.dailyNews.base

import android.content.BroadcastReceiver
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jw.dailyNews.BaseApplication
import com.jw.dailyNews.utils.CacheUtils
import com.jw.dailyNews.utils.ThemeUtils
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/11/2 16:31
 * 版本：
 * 作者：Mr.jin
 */

abstract class BaseActivity : AppCompatActivity() {

    protected var receivers: List<BroadcastReceiver> = ArrayList()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        (application as BaseApplication).addActivity(this)
        bindView()
        init()
        initView()
        loadData()
        initEvent()
    }

    /**
     * 在初始化视图之前执行，如注册广播接收者，还有一些初始化操作
     */
    protected fun init() {

    }

    /**
     * 绑定视图
     */
    protected abstract fun bindView()

    /**
     * 初始化视图
     */
    protected open fun initView() {}

    /**
     * 初始化监听事件
     */
    protected open fun initEvent() {

    }

    /**
     * 读取数据
     */
    protected fun loadData() {

    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.changeStatusBar(this, CacheUtils.getCacheInt("indicatorColor", Color.RED))
        loadData()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (receivers.size != 0) {
            for (i in receivers) {
                unregisterReceiver(i)
            }
        }
        (application as BaseApplication).removeActivity(this)
    }
}
