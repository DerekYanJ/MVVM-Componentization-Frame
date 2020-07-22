package com.yqy.common.base.dialogfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blankj.utilcode.util.ConvertUtils

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
abstract class BaseDialogFragment : AbsDialogFragment() {

    private var isInit = false // 是否已经初始化了
    private lateinit var rootView: View

    private var mOnDismissListener: OnDismissListener? = null

    fun setDismissListener(mOnDismissListener: OnDismissListener) {
        this.mOnDismissListener = mOnDismissListener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val contentViewId: Int = getLayoutId()
        if (contentViewId <= 0)
            throw RuntimeException("You must set the layout ID of DialogFragment")

        rootView = inflater.inflate(contentViewId, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isInit = true
        initView(savedInstanceState)
        initData()
    }


    override fun onStart() {
        dialog?.window?.attributes?.width =
            resources.displayMetrics.widthPixels - ConvertUtils.dp2px(80f)
        super.onStart()
        activity?.windowManager?.defaultDisplay?.getMetrics(DisplayMetrics())
        /*dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )*/
    }


    override fun <V : View> findViewById(viewId: Int): V {
        if (!isInit)
            throw RuntimeException("DialogFragment not initialized")
        return rootView.findViewById(viewId)
    }


    override fun show(manager: FragmentManager, tag: String?) {
        try {
            try {
                if (!isAdded && !isRemoving && !isVisible) {
                    val fragment: Fragment? = manager.findFragmentByTag(tag)
                    if (fragment != null) {
                        manager.beginTransaction().remove(fragment).commitAllowingStateLoss()
                    }
                    super.show(manager, tag)
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                if (!isAdded && !isRemoving && !isVisible) {
                    // 解决java.lang.IllegalStateException: Can not perform this action问题
                    val ft: FragmentTransaction = manager.beginTransaction()
                    ft.add(this, tag)
                    ft.commitAllowingStateLoss()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        try {
            super.dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
            super.dismiss()
        }
        mOnDismissListener?.onDismiss()
    }

    interface OnDismissListener{
        fun onDismiss()
    }
}