package com.yqy.common.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
abstract class AbsActivity : AppCompatActivity() {

    /**
     * layout Id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * onCreate() 中 setContentView 之前
     */
    protected abstract fun setContentViewBefore()

    /**
     * onCreate() 中 super.onCreate 之前
     */
    protected abstract fun superOnCreateBefore()

    /**
     * initView() 方法调用之前
     */
    protected abstract fun initViewBefore()

    /**
     * 状态栏是否是暗色
     */
    protected abstract fun isDarkStatusBar(): Boolean

    /**
     * 设置状态栏
     */
    protected abstract fun initStatusBar()

    /**
     * 设置导航栏
     */
    protected abstract fun initNavigationBar()

    /**
     * 初始化 view
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 是否显示 Toolbar
     */
    protected abstract fun isShowToolbar(): Boolean

    /**
     * 初始化 arguments 参数
     */
    protected abstract fun initParameters()
}