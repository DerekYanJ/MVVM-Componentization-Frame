package com.yqy.common.third.arouter

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

/**
 * @desc
 * @author derekyan
 * @date 2020/6/30
 */
@Interceptor(priority = 7, name = "登录拦截器")
class ARouterInterceptor: IInterceptor {
    var mContext: Context? = null
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        //TODO 判断是否需要登录
        println("ARouterInterceptor process()")
        callback?.onContinue(postcard)
    }

    override fun init(context: Context?) {
        mContext = context
        println("ARouterInterceptor init()")
    }
}