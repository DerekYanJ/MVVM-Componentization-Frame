package com.yqy.common.ui.dialog

import android.os.Bundle
import com.zs.common.R
import com.yqy.common.base.dialogfragment.BaseDialogFragment

/**
 * @desc
 * @author derekyan
 * @date 2020/6/29
 */
class LoadingDialog: BaseDialogFragment() {
    override fun getLayoutId(): Int = R.layout.dialog_loading

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }
}