package com.yqy.photo.glide

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.yqy.photo.imageloader.IImageLoader
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class GlideUtil : IImageLoader {

    override fun loadImg(imgUrl: String, img: ImageView, placeHolder: Int?) {
        if (placeHolder != null) {
            Glide.with(img.context)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeHolder)
                .into(img)
        } else {
            Glide.with(img.context)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(img)
        }
    }


    fun loadImgTop(imgUrl: String, img: ImageView, width: Int, height: Int, placeHolder: Int = 0) {
        val multi = MultiTransformation(
            CropTransformation(width, height, CropTransformation.CropType.TOP)
        )
        val baseRequestOptions = Glide.with(img.context)
            .load(imgUrl)
            .apply(RequestOptions.bitmapTransform(multi))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        if (placeHolder != 0) {
            baseRequestOptions.placeholder(placeHolder).error(placeHolder)
        }
        baseRequestOptions.into(img)
    }


    // 图片-模糊
    override fun loadImgWithBlur(
        imgUrl: String,
        img: ImageView,
        blur: Int,
        placeHolder: Int
    ) {
        val baseRequestOptions = Glide.with(img.context)
            .load(imgUrl)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(blur)))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        if (placeHolder != 0) {
            baseRequestOptions.placeholder(placeHolder).error(placeHolder)
        }
        baseRequestOptions.into(img)
    }


    //圆角
    override fun loadImgWithRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        placeHolder: Int
    ) {
        val multi = MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(radius, 0)
        )
        val baseRequestOptions = Glide.with(img.context)
            .load(imgUrl)
            .apply(RequestOptions.bitmapTransform(multi))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        if (placeHolder != 0) {
            baseRequestOptions.placeholder(placeHolder).error(placeHolder)
        }
        baseRequestOptions.into(img)
    }

    //圆角
    fun loadImgWithRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Float,
        placeHolder: Int = 0
    ) {
        loadImgWithRoundCorner(
            imgUrl,
            img,
            radius.toInt(),
            placeHolder
        )
    }


    //圆形
    override fun loadCircleImg(imgUrl: String?, img: ImageView) {
        if (TextUtils.isEmpty(imgUrl)) return
        Glide.with(img.context)
            .load(imgUrl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(img)
    }

    // 圆形-模糊
    override fun loadCircleImgWithBlur(
        imgUrl: String,
        img: ImageView,
        blur: Int,
        placeHolder: Int
    ) {
        val multi = MultiTransformation(
            BlurTransformation(blur),
            CircleCrop()
        )
        val baseRequestOptions = Glide.with(img.context)
            .load(imgUrl)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(multi))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        if (placeHolder != 0) {
            baseRequestOptions.placeholder(placeHolder).error(placeHolder)
        }
        baseRequestOptions.into(img)
    }

    override fun loadCircleImgWithBorder(
        imgUrl: String,
        img: ImageView,
        borderSize: Int,
        borderColor: Int,
        placeHolder: Int
    ) {
        val multi = MultiTransformation(
            CropCircleWithBorderTransformation(borderSize, borderColor)
        )
        val baseRequestOptions = Glide.with(img.context)
            .load(imgUrl)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(multi))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        if (placeHolder != 0) {
            baseRequestOptions.placeholder(placeHolder).error(placeHolder)
        }
        baseRequestOptions.into(img)
    }

    // 顶部圆角
    override fun loadImgWithTopRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        placeHolder: Int
    ) {
        loadImgWithSpacialRoundCorner(
            imgUrl,
            img,
            radius,
            RoundedCornersTransformation.CornerType.TOP,
            placeHolder
        )
    }

    // 底部圆角
    override fun loadImgWithBottomRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        placeHolder: Int
    ) {
        loadImgWithSpacialRoundCorner(
            imgUrl,
            img,
            radius,
            RoundedCornersTransformation.CornerType.BOTTOM,
            placeHolder
        )
    }

    // 可配置的圆角
    @SuppressLint("CheckResult")
    override fun loadImgWithSpacialRoundCorner(
        imgUrl: String,
        img: ImageView,
        radius: Int,
        cornerType: RoundedCornersTransformation.CornerType,
        placeHolder: Int
    ) {
        val multi = MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(radius, 0, cornerType)
        )
        val baseRequestOptions = Glide.with(img.context)
            .load(imgUrl)
            .apply(RequestOptions.bitmapTransform(multi))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        if (placeHolder != 0) {
            baseRequestOptions.placeholder(placeHolder).error(placeHolder)
        }
        baseRequestOptions.into(img)
    }


    // Glide Url->Bitmap
    fun convertUrl2Bitmap(context: Context, imgUrl: String, callback: IImageLoader.Url2BitmapCallback) {
        val multi = MultiTransformation(
            CenterCrop(),
            RoundedCornersTransformation(30, 0)
        )
        Glide.with(context)
            .asBitmap()
            .load(imgUrl)
            .apply(RequestOptions.bitmapTransform(multi))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//            .placeholder(R.drawable.place_holder_square)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback.url2BitmapFailed(e)
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    callback.url2BitmapSuccess(resource)
                    return false
                }

            })
            .submit()
    }




}