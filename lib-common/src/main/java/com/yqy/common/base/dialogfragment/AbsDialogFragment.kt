package com.yqy.common.base.dialogfragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
abstract class AbsDialogFragment: DialogFragment() {

    /**
     * layout Id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化 view
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()


    protected abstract fun <V : View> findViewById(viewId: Int): V
}