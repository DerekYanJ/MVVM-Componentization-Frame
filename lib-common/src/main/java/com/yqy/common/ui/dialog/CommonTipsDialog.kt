package com.yqy.common.ui.dialog

import android.os.Bundle
import android.view.View
import com.zs.common.R
import com.yqy.common.base.dialogfragment.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_common_tips.*

/**
 * 统一的提示Dialog
 * Created by zsl on 2020/5/9.
 */

class CommonTipsDialog(
    private var title: String = "",
    private var content: String = "",
    private var confirmStr: String = "确定",
    private var cancelStr: String = "取消",
    private var hideCancel: Boolean = false,
    private var confirmListener: () -> Unit = {},
    private var cancelListener: () -> Unit = {}
) : BaseDialogFragment() {

    override fun getLayoutId(): Int = R.layout.dialog_common_tips

    override fun initView(savedInstanceState: Bundle?) {
        tv_title.text = title
        tv_content.text = content
        btn_confirm.text = confirmStr
        tv_cancel.text = cancelStr

        if (hideCancel) tv_cancel.visibility = View.GONE

        btn_confirm.setOnClickListener {
            confirmListener.invoke()
            dismiss()
        }

        tv_cancel.setOnClickListener {
            cancelListener.invoke()
            dismiss()
        }
    }

    override fun initData() {
    }


}