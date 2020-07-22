package com.yqy.common.third.bugly

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport
import com.zs.common.BuildConfig

/**
 * @desc 异常上报
 * @author derekyan
 * @date 2020/5/14
 */
object CrashReport {

    fun init(context: Application) {
        CrashReport.initCrashReport(context, BuglyConstants.getAppId(), BuildConfig.ENV_TEST)
    }

    fun reportException(e: Throwable?) {
        CrashReport.postCatchedException(e)
    }

    fun setUserId(userId: String) {
        CrashReport.setUserId(userId)
    }

}