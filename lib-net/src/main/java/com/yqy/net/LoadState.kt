package com.yqy.net

import com.yqy.net.error.ApiException

/**
 * @desc
 * @author derekyan
 * @date 2020/5/15
 */
sealed class LoadState(val msg: String, val apiException: ApiException?) {
    /**
     * 加载中
     */
    class Loading(msg: String = ""): LoadState(msg, null)

    /**
     * 成功
     */
    class Success(msg: String = ""): LoadState(msg, null)

    /**
     * 失败
     */
    class Fail(msg: String = ""): LoadState(msg, null)

    class FailApi(e: ApiException): LoadState(e.msg, e)
}