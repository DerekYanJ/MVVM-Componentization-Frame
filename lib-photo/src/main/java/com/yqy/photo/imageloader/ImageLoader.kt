package com.yqy.photo.imageloader

import android.widget.ImageView
import androidx.annotation.ColorInt
import com.yqy.photo.glide.GlideUtil
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * @desc
 * @author derekyan
 * @date 2020/5/14
 */
object ImageLoader {
    val imageLoader: IImageLoader = GlideUtil()

    fun loadImg(imgUrl: String, img: ImageView, placeHolder: Int? = 0) {
        imageLoader.loadImg(imgUrl, img, placeHolder)
    }

    // 图片-模糊
    fun loadImgWithBlur(
        imgUrl: String,
        img: ImageView,
        blur: Int = 25,
        placeHolder: Int = 0
    ) {
        imageLoader.loadImgWithBlur(imgUrl, img, blur, placeHolder)
    }

    //圆角
    fun loadImgWithRoundCorner(imgUrl: String, img: ImageView, radius: Int, placeHolder: Int = 0) {
        imageLoader.loadImgWithRoundCorner(imgUrl, img, radius, placeHolder)
    }

    //圆形
    fun loadCircleImg(imgUrl: String?, img: ImageView) {
        imageLoader.loadCircleImg(imgUrl, img)
    }

    // 圆形-模糊
    fun loadCircleImgWithBlur(
        imgUrl: String,
        img: ImageView,
        blur: Int = 25,
        placeHolder: Int = 0
    ) {
        imageLoader.loadCircleImgWithBlur(imgUrl, img, blur, placeHolder)
    }

    // 圆形-边框
    fun loadCircleImgWithBorder(
        imgUrl: String,
        img: ImageView,
        borderSize: Int = 5,
        @ColorInt borderColor: Int,
        placeHolder: Int = 0
    ) {
        imageLoader.loadCircleImgWithBorder(imgUrl, img, borderSize, borderColor, placeHolder)
    }

    // 顶部圆角
    fun loadImgWithTopRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        placeHolder: Int = 0
    ) {
        imageLoader.loadImgWithTopRoundCorner(imgUrl, img, radius, placeHolder)
    }

    // 底部圆角
    fun loadImgWithBottomRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        placeHolder: Int = 0
    ) {
        imageLoader.loadImgWithBottomRoundCorner(imgUrl, img, radius, placeHolder)
    }

    // 可配置的圆角
    fun loadImgWithSpacialRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        cornerType: RoundedCornersTransformation.CornerType,
        placeHolder: Int = 0
    ) {
        imageLoader.loadImgWithSpacialRoundCorner(imgUrl, img, radius, cornerType, placeHolder)
    }



}