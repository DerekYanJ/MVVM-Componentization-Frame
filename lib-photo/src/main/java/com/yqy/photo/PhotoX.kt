package com.yqy.photo

import androidx.fragment.app.FragmentActivity
import com.yqy.photo.choose.PhotoBuilder

/**
 * @desc
 * @author derekyan
 * @date 2020/7/10
 */
object PhotoX {

    fun init(activity: FragmentActivity): PhotoBuilder {
        return PhotoBuilder(activity)
    }

}