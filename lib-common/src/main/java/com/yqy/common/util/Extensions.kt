package com.yqy.common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import com.orhanobut.logger.Logger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * @desc
 * @author derekyan
 * @date 2020/5/15
 */

fun getImgMultiBody(params: HashMap<String, Any>?,
                 imgFiles: String): MultipartBody {

    if(params != null) Logger.e(params.toString())
    Logger.e("上传的文件：imgFiles:$imgFiles ")

    val builder = MultipartBody.Builder()
    val tempList = handleImagesWithMatrix(mutableListOf(imgFiles))

    var index = 0

    //添加图片
    var totalLength: Long = 0
    for (i in tempList.indices) {
        index = i
        val path = tempList[i]
        val file = File(path)
        totalLength += file.length()
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        builder.addFormDataPart("file", file.name, requestBody)
    }

    //添加参数
    if (params != null) {
        val iter = params.entries.iterator()

        while (iter.hasNext()) {
            val entry = iter.next() as Map.Entry<*, *>
            val key = entry.key
            val `val` = entry.value
            builder.addFormDataPart("content", `val`.toString())
        }
    }

    builder.setType(MultipartBody.FORM)

    Logger.e("totalLength = $totalLength")

    return builder.build()
}

private fun handleImagesWithMatrix(files: MutableList<String>): MutableList<String> {
    val tempList = mutableListOf<String>()
    val start = System.currentTimeMillis()
    files.forEach {
        val file = File(it)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false//耗内存
        val source = BitmapFactory.decodeFile(it, options)//因为
        val width = options.outWidth
        val height = options.outHeight

        val referenceWidth = 1280
        Logger.e("file.length is ${file.length() / 1000}KB,w*h is $width*$height")

        if (width <= referenceWidth && height <= referenceWidth) {
            //不处理，使用原图
            Logger.e("the width and height are both less than the reference，tempFile is not scaled")
            tempList.add(it)
        } else if (width > referenceWidth && height > referenceWidth) {//宽高都大于1280
            //较小设置为1280，较大等比压缩
            Logger.e("the width and height are both greater than the reference,temp will be scaled")
            val min = Math.min(width, height)
            val scale = if (min == width) {
                referenceWidth / width.toFloat()
            } else {
                referenceWidth / height.toFloat()
            }
            Logger.e("both greater scale is $scale")
            val tempFilePath = getScaledFile(scale, source, file)
            tempList.add(tempFilePath)
        } else {//一个大于1280，另外一个小于1280
            if (width / height > 2 || height / width > 2) {//宽高比大于2//改为大于2，目前不少全面屏截图均为2/1，但实际图片并不过于大。
                //不处理
                Logger.e("either width or height is greater than the reference,and the ratio is more than two,don't scale it")
                tempList.add(it)
            } else {//宽高比小于2，设置最大边为1280，小边等比压缩
                //3.20增加判断，如果图片大小小于500K，则不必压缩。
                if (file.length() < 200 * 1024) {
                    Logger.e("one of the height and width is greater than the reference,and the ratio is less than two," +
                            "but the size of file is less than 500KB,don't scale it.")
                    tempList.add(it)
                } else {
                    Logger.e("one of the height and width is greater than the reference,and the ratio is less than two,scale it.")
                    val max = Math.max(width, height)
                    val scale = if (max == width) referenceWidth / width.toFloat() else referenceWidth / height.toFloat()
//                    Logger.e("one of scale = $scale")
                    val tempFilePath = getScaledFile(scale, source, file)
                    tempList.add(tempFilePath)
                }
            }
        }
    }
    Logger.e("Matrix压缩耗时：${System.currentTimeMillis() - start}毫秒")
    return tempList
}


private fun getScaledFile(scale: Float, source: Bitmap, file: File): String {
    val matrix = Matrix()
    matrix.setScale(scale, scale)
    val bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    val tempFilePath = Environment.getExternalStorageDirectory().absolutePath + "/xh/img" + "/" + file.name + "-temp" + ".jpg"
    val tempNewFile = newFile(tempFilePath)
    val bos = BufferedOutputStream(FileOutputStream(tempNewFile))
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos)
//        Logger.e("both greater tempFile.length = ${File(tempFilePath).length() / 1000}KB,and w*h is ${width * scale}*${height * scale}")
    bos.flush()
    bos.close()
    bitmap.recycle()
    return tempFilePath

}


fun newFile(filePath: String): File {
    val file = File(filePath)
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    return file
}
