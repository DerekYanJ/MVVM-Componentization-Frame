package com.yqy.net.gson

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.yqy.net.AbsResult
import com.yqy.net.error.ApiException
import com.yqy.net.ExceptionHandle
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import kotlin.text.Charsets.UTF_8


/**
 * 返回结果包装
 */
class  CustomGsonResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        val response = value.string()

        //获取到返回的response字符串，并解析成APIResponse格式。如果code==0，则为正确返回，直接read,
        //如果code！=0，则是错误返回，此处应处理成通用错误数据。暂时直接抛异常看能否收到信息。
        var apiResponse: AbsResult<T>? = null
        try {
            apiResponse = gson.fromJson(response, AbsResult::class.java) as AbsResult<T>?
            //应对特殊返回数据格式：
            //{"code":500,"message":"系统开小差了,请稍等片刻","data":{}}
            //需要给 msg 字段赋有效值 message
            if(apiResponse?.msg.isNullOrEmpty() && apiResponse?.msg != null) apiResponse.msg = apiResponse.msg
        } catch (e: Exception) {
            e.printStackTrace()
//            throw ApiException("-3", "服务器解析错误！")
            throw ExceptionHandle.handleException(e)
        } finally {
            if (apiResponse == null) {
                throw ApiException("-3", "服务器解析错误！")
            }
        }

        //接口请求正确返回
        if (apiResponse.status != "-100") {
            try {
                try {
                    val contentType = value.contentType()
                    val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
                    val inputStream = ByteArrayInputStream(response.toByteArray())
                    val reader = InputStreamReader(inputStream, charset)
                    val jsonReader = gson.newJsonReader(reader)
                    return adapter.read(jsonReader)
                } catch (e1: Exception) {
                    e1.printStackTrace()
//                    throw ApiException("-2", "JSON解析失败${e1.message}")
                    throw ExceptionHandle.handleException(e1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is ApiException) {
                    throw e
                } else {
//                    throw ApiException("-2", "JSON解析失败${e.message}")
                    throw ExceptionHandle.handleException(e)
                }
            } finally {
                value.close()
            }
        } else {
            value.close()
            throw ExceptionHandle.handleException(apiResponse.status, apiResponse.msg)
//            throw ApiException(apiResponse.status, apiResponse.msg)
        }
    }
}
