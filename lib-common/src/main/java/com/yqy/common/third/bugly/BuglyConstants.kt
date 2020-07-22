package com.yqy.common.third.bugly

import com.zs.common.BuildConfig

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
object BuglyConstants {

    private const val APP_ID_TEST = "c31d5cb661"
    private const val APP_ID_PROD = "e2df63298b"

    fun getAppId(): String {
        return if (BuildConfig.ENV_TEST) APP_ID_TEST else APP_ID_PROD
    }
}