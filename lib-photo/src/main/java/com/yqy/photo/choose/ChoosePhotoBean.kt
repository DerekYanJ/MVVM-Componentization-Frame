package com.yqy.photo.choose

import android.graphics.Bitmap

/**
 * @desc
 * @author derekyan
 * @date 2020/7/10
 */
class ChoosePhotoBean {
    var isShowTakePhoto = false
    var isShowPhotoAlbum = false
    var isShowSaveToAlbum = false

    var isNeedCrop = false
    var xCropRadio = 1f
    var yCropRadio = 1f

    var photoPath = ""
    var cropPhotoPath = ""

    var photoBitmap: Bitmap? = null
}