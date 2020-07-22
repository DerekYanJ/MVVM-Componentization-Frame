package com.yqy.common.base

import com.yqy.net.error.ApiException

/**
 * @desc
 * @author derekyan
 * @date 2020/5/15
 */
interface IBaseView {

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

    fun showToast(msg: String)

    fun showError(apiException: ApiException)
}