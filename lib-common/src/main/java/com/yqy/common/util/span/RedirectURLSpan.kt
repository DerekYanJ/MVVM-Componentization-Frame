package com.yqy.common.util.span

import android.content.Context
import android.text.style.URLSpan
import android.view.View
import com.yqy.common.ToastUtil

/**
 * @desc url中超链接的处理
 * @author derekyan
 * @date 2020/7/3
 */
class RedirectURLSpan : URLSpan {
    private var mUrl: String = ""
    private lateinit var context: Context

    constructor(context: Context, url: String): super(url) {
        this.context = context
        mUrl = url
    }

    companion object {
        const val USER_PROTOCOL = "user_protocol" // 用户协议
        const val PRIVACY_POLICY = "privacy_Policy" // 隐私政策
        const val CERT_Service = "certification_service" //认证服务


        /**
         * 判断是否需要重新设置超链接颜色值
         *
         * @param url
         * @return
         */
        fun isResetUrlColor(url: String): Boolean {
            if (!url.isNullOrEmpty()) {
                if (url.contains(USER_PROTOCOL) ||
                    url.contains(PRIVACY_POLICY) ||
                    url.contains(CERT_Service)
                ) {
                    return true
                }
            }
            return false
        }
    }

    override fun onClick(widget: View) {
        super.onClick(widget)
        if (!mUrl.isNullOrEmpty()) {
            if (mUrl.startsWith("http://") || mUrl.startsWith("https://") || mUrl.startsWith("www.")) {
                // 外部链接
                //TODO 跳转webview
            } else if (mUrl.contains(USER_PROTOCOL)) {
                //TODO 用户隐私
                ToastUtil.show(context, "用户隐私")
            } else if (mUrl.contains(PRIVACY_POLICY)) {
                //TODO 隐私政策
                ToastUtil.show(context, "隐私政策")
            } else if (mUrl.contains(CERT_Service)) {
                //TODO 认证服务
                ToastUtil.show(context, "认证服务")
            }
        }
    }

}