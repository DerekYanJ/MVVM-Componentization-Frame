package com.yqy.common.third.arouter

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @desc
 * @author derekyan
 * @date 2020/6/30
 */
object ARouterManager {

    /**
     * 跳转操作
     */
    fun start(
        path: String,
        bundle: Bundle? = null,
        objectMap: Map<String, Any>? = null,
        activity: Activity? = null,
        requestCode: Int? = null
    ) {
        val postCard = ARouter.getInstance().build(path)

        if (bundle != null)
            postCard.with(bundle)

        objectMap?.forEach {
            postCard.withObject(it.key, it.value)
        }


        if (activity != null && requestCode != null)
            postCard.navigation(activity, requestCode)
        else
            postCard.navigation()
    }

    /**
     * 获取fragment
     */
    fun getFragment(path: String): Fragment {
        return ARouter.getInstance().build(path).navigation() as Fragment
    }
}