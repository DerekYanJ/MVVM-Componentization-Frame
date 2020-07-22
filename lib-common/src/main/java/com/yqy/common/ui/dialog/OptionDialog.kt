package com.yqy.common.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zs.common.R

/**
 * @desc 底部弹出选项提示框 BottomSheetDialog
 * @author derekyan
 * @date 2020/7/13
 */
object OptionDialog {

    /*
    val optionList = arrayListOf<OptionDialog.OptionBean>()
    optionList.add(OptionDialog.OptionBean("操作1", View.OnClickListener {
        showToast("操作1")
    }))
    optionList.add(OptionDialog.OptionBean("操作2", View.OnClickListener {
        showToast("操作2")
    }))
    optionList.add(OptionDialog.OptionBean("操作3", View.OnClickListener {
        showToast("操作3")
    }))
    optionList.add(OptionDialog.OptionBean("操作4", View.OnClickListener {
        showToast("操作5")
    }))
    OptionDialog.build(requireContext(),optionList, OptionDialog.OptionBean("取消234", View.OnClickListener {
        showToast("取消123456")
    })).show()
    */

    fun build(
        context: Context,
        optionList: List<OptionBean> = mutableListOf(),
        cancelOptionBean: OptionBean? = null
    ): BottomSheetDialog {
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.dialog_option)
        val ll_option = bottomSheetDialog.findViewById<LinearLayout>(R.id.ll_option)
        val tv_cancel = bottomSheetDialog.findViewById<TextView>(R.id.tv_cancel)

        // 动态添加option
        optionList.forEach { optionBean ->
            // 设置option
            val view_option = LayoutInflater.from(context).inflate(R.layout.item_option_dialog, null) as LinearLayout
            val tv_option = view_option.findViewById<TextView>(R.id.tv_option_name)
            val iv_option = view_option.findViewById<ImageView>(R.id.iv_option)
            if (optionBean.imgResId != -1) {
                iv_option.visibility = View.VISIBLE
                iv_option.setImageResource(optionBean.imgResId)
            }
            tv_option.text = optionBean.name
            view_option.setOnClickListener {
                optionBean.listener?.onClick(it)
                bottomSheetDialog.dismiss()
            }
            ll_option?.addView(view_option)
        }

        // 取消按钮的设置
        if (cancelOptionBean != null) {
            tv_cancel?.text = cancelOptionBean.name
            tv_cancel?.setOnClickListener {
                cancelOptionBean.listener?.onClick(it)
                bottomSheetDialog.dismiss()
            }
        } else {
            tv_cancel?.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        return bottomSheetDialog
    }


    data class OptionBean(
        var name: String = "",
        var listener: View.OnClickListener? = null,
        var imgResId: Int = -1
    )

}