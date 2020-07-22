package com.yqy.common.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

/**
 * Created by zsl on 2020/3/13.
 */
class FileUtil {
    companion object {

        fun getLocalCachePath(): String {
            val status: String = Environment.getExternalStorageState()
            val path = Environment.getExternalStorageDirectory().path + File.separator + "zslive"
            if (status == Environment.MEDIA_MOUNTED) {
                val fileDir = File(path)
                if (!fileDir.exists()) {
                    fileDir.mkdirs()
                }
                return path;
            }
            return  Environment.getExternalStorageDirectory().path
        }


        //创建保存路径
        fun createDir(saveAddress: String) {
            val status: String = Environment.getExternalStorageState()
            if (status == Environment.MEDIA_MOUNTED) {
                val desDir = File(saveAddress)
                if (!desDir.exists()) {
                    desDir.mkdirs()
                }
            }
        }


        //保存网络图片到本地
        fun saveNetImgToLocal(imgUrl: String, savePath: String, fileName: String) {
            Thread {
                val url = URL(imgUrl)
                val con: URLConnection = url.openConnection()
                con.connectTimeout = 5 * 1000
                val inputStream: InputStream = con.getInputStream()
                val bs = ByteArray(1024)//1K的数据缓冲
                var len: Int
                val status: String = Environment.getExternalStorageState()
                if (status == Environment.MEDIA_MOUNTED) {
                    val fileDir = File(savePath)
                    if (!fileDir.exists()) {
                        fileDir.mkdirs()
                    }
                    try {
                        val file: String = fileDir.path + "/" + fileName
                        val outputStream = FileOutputStream(file)
                        while (inputStream.read(bs).also { len = it } != -1) {
                            outputStream.write(bs, 0, len)
                        }
                        outputStream.close()
                        inputStream.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
//                    ToastUtil.show("外部存储设备未挂载！")
                }
            }.start()
        }

        fun getGlideCachePath(context: Context): String {
            return context.cacheDir.path + "/GlideCacheFolder"
        }
    }
}