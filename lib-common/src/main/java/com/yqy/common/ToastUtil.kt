package com.yqy.common

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.zs.common.R

/**
 * @desc
 * @author derekyan
 * @date 2020/5/15
 */
class ToastUtil {
    companion object {
        private var toast: Toast? = null
        private lateinit var tvMsg: TextView

        fun show(context: Context, msg: String) {
            if (toast == null) {
                toast = Toast(context)
                val toastLayout: View =
                    LayoutInflater.from(context).inflate(R.layout.toast_layout, null)
                tvMsg = toastLayout.findViewById(
                    R.id.tv_msg
                )
                toast?.view = toastLayout
                toast?.setGravity(Gravity.CENTER, 0, 0)
                toast?.duration = Toast.LENGTH_SHORT
            }
            tvMsg.text = msg
            toast?.show()
        }
    }
}