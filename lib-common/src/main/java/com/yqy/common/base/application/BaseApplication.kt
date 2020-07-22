package com.yqy.common.base.application

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.zs.common.BuildConfig
import com.yqy.common.third.bugly.CrashReport

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initBugly()
    }


    /**
     * 组件Application动态配置，核心通过反射，不想主模块直接访问组件中的类
     */
    abstract fun initModuleApp(application: Application)

    abstract fun initModuleAppAfter(application: Application)

    abstract fun destroyApp()


    /**
     * 初始化bugly
     */
    private fun initBugly() {
        CrashReport.init(this)
    }

    private fun initLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
            .methodCount(2) // (Optional) How many method line to show. Default 2
            .methodOffset(5) // (Optional) Hides internal method calls up to offset. Default 5
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.ENV_TEST
            }
        })
    }

}