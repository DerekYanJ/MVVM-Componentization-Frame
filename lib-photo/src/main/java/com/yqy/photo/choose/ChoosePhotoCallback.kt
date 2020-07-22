package com.yqy.photo.choose

/**
 * @desc 选择图片回调
 * @author derekyan
 * @date 2020/7/10
 */
interface ChoosePhotoCallback {
    fun onSuccess(photoPath: String)
    fun onFailed(errorType: Int, errorMsg: String)
    fun onCancel()
}