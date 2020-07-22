package com.yqy.photo.choose

import androidx.fragment.app.FragmentActivity

/**
 * @desc
 * @author derekyan
 * @date 2020/7/10
 */
class PhotoBuilder {
    val FRAGMENT_TAG = "${this.javaClass.canonicalName}_Fragment"

    var activity: FragmentActivity
    private var choosePhotoBean: ChoosePhotoBean

    private var mChoosePhotoCallback: ChoosePhotoCallback? = null

    constructor(activity: FragmentActivity) {
        this.activity = activity
        choosePhotoBean = ChoosePhotoBean()
    }

    /**
     * 拍照图片保存路径
     */
    fun setPhotoPath(photoPath: String): PhotoBuilder {
        if (photoPath.isEmpty())
            throw RuntimeException("photoPath 不能为空")
        choosePhotoBean.photoPath = photoPath
        return this
    }

    /**
     * 拍照
     */
    fun showTakePhoto(): PhotoBuilder {
        choosePhotoBean.isShowTakePhoto = true
        return this
    }

    /**
     * 相册
     */
    fun showPhotoAlbum(): PhotoBuilder {
        choosePhotoBean.isShowPhotoAlbum = true
        return this
    }

    /**
     * 保存到相册
     */
    fun showSaveToAlbum(): PhotoBuilder {
        choosePhotoBean.isShowSaveToAlbum = true
        return this
    }

    /**
     * 需要裁剪
     * cropPhotoPath: 裁剪之后的图片路径
     */
    fun needCrop(cropPhotoPath: String): PhotoBuilder {
        choosePhotoBean.isNeedCrop = true
        choosePhotoBean.cropPhotoPath = cropPhotoPath
        return this
    }

    /**
     * 设置裁剪比例 x：宽  y：高 默认 1:1
     */
    fun setCropRadio(x: Float, y: Float): PhotoBuilder  {
        choosePhotoBean.xCropRadio = x
        choosePhotoBean.yCropRadio = y
        return this
    }

    fun setCallback(mChoosePhotoCallback: ChoosePhotoCallback): PhotoBuilder {
        this.mChoosePhotoCallback = mChoosePhotoCallback
        return this
    }

    /**
     * 打开dialog
     */
    fun showDialog() {
        getInvisibleFragment().showDialog(this)
    }


    fun getPhotoBean(): ChoosePhotoBean {
        return choosePhotoBean
    }

    fun getChooseCallback() : ChoosePhotoCallback? {
        return mChoosePhotoCallback
    }


    private fun getInvisibleFragment(): InvisiblePhotoFragment {
        val fragmentManager =
            activity.supportFragmentManager
        val existedFragment =
            fragmentManager.findFragmentByTag(FRAGMENT_TAG)
        return if (existedFragment != null) {
            existedFragment as InvisiblePhotoFragment
        } else {
            val invisibleFragment = InvisiblePhotoFragment()
            fragmentManager.beginTransaction()
                .add(invisibleFragment, FRAGMENT_TAG).commitNow()
            invisibleFragment
        }
    }

}