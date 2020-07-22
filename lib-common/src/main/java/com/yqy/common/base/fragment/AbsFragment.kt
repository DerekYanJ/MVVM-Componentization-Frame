package com.yqy.common.base.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
abstract class AbsFragment: Fragment() {
    /**
     * layout Id
     */
    protected abstract fun getLayoutId(): Int

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

    protected abstract fun <V : View> findViewById(viewId: Int): V

    /**
     * 初始化 arguments 参数
     */
    protected abstract fun initParameters()

    /**
     * initView() 方法调用之前
     */
    protected abstract fun initViewBefore()
}