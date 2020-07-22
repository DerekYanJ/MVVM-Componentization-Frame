package com.yqy.photo.imageloader

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.ColorInt
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
interface IImageLoader {
    fun loadImg(imgUrl: String, img: ImageView, placeHolder: Int?)

    // 图片-模糊
    fun loadImgWithBlur(
        imgUrl: String,
        img: ImageView,
        blur: Int = 25,
        placeHolder: Int = 0
    )

    //圆角
    fun loadImgWithRoundCorner(imgUrl: String, img: ImageView, radius: Int, placeHolder: Int = 0)

    //圆形
    fun loadCircleImg(imgUrl: String?, img: ImageView)

    // 圆形-模糊
    fun loadCircleImgWithBlur(
        imgUrl: String,
        img: ImageView,
        blur: Int = 25,
        placeHolder: Int = 0
    )

    // 圆形-边框
    fun loadCircleImgWithBorder(
        imgUrl: String,
        img: ImageView,
        borderSize: Int = 5,
        @ColorInt borderColor: Int,
        placeHolder: Int = 0
    )

    // 顶部圆角
    fun loadImgWithTopRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        placeHolder: Int = 0
    )

    // 底部圆角
    fun loadImgWithBottomRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        placeHolder: Int = 0
    )

    // 可配置的圆角
    fun loadImgWithSpacialRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        cornerType: RoundedCornersTransformation.CornerType,
        placeHolder: Int = 0
    )

    interface Url2BitmapCallback {
        fun url2BitmapSuccess(bitmap: Bitmap?)
        fun url2BitmapFailed(e: Exception?)
    }
}