package com.yqy.common.manager

import com.blankj.utilcode.util.SPUtils

/**
 * @desc
 * @author derekyan
 * @date 2020/7/1
 */
object SpManager {
    val spName = "zs-live"


    fun getSp(): SPUtils {
        return SPUtils.getInstance(spName)
    }
}