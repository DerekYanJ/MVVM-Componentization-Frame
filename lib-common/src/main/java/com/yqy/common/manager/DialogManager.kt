package com.yqy.common.manager

import androidx.fragment.app.FragmentActivity
import com.yqy.common.ui.dialog.LoadingDialog

/**
 * @desc
 * @author derekyan
 * @date 2020/6/29
 */
object DialogManager {

    private var mDialog: LoadingDialog? = null
    private var loadDialogTag = "loadingDialog"

    fun showLoadingDialog(activity: FragmentActivity? = null, isCancelable: Boolean = false) {
        // mDialog非空时 需判断是否已经打开 若已打开则停止执行
        if (mDialog != null && mDialog?.isVisible == true) {
            return
        }

        // 获取activity对象
        val showActivity =
            activity ?: ActivityManager.taskTopActivity ?: return

        if (mDialog == null ) {
            mDialog = LoadingDialog()
            mDialog?.isCancelable = isCancelable
        }

        // 打开弹窗
        mDialog?.show(showActivity.supportFragmentManager, loadDialogTag)
    }

    fun dismissLoadingDialog() {
        mDialog?.dismiss()
    }

    fun clearDialog() {
        mDialog = null
    }
}