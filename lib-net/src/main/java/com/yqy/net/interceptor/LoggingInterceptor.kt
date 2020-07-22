package com.yqy.net.interceptor

import okhttp3.*
import java.io.EOFException
import java.nio.charset.Charset

/**
 * Created by zsl on 2020/3/18.
 */
class LoggingInterceptor : Interceptor {

    private val utf8: Charset = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)

        /**
         * 这里不能直接使用response.body().string()的方式输出日志
         * 因为response.body().string()之后，response中的流会被关闭，
         * 程序会报错，我们需要创建出一个新的response给应用层处理
         */
        val responseBody: ResponseBody = response.peekBody(1024 * 1024)
        var params = ""
        val requestBody: RequestBody? = request.body
        if (requestBody != null) {
            val buffer = okio.Buffer()
            requestBody.writeTo(buffer)
            val contentType: MediaType? = requestBody.contentType()
            if (contentType != null) {
                val charset: Charset = contentType.charset(utf8)!!
                if (isPlaintext(buffer)) {
                    params = buffer.readString(charset)
                }
            }
        }
        val format = "%s\nresponse.code=%s\n%s\n%s"
        return response
    }

    private fun isPlaintext(buffer: okio.Buffer): Boolean {
        return try {
            val prefix = okio.Buffer()
            val byteCount: Long = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i: Int in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint: Int = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) &&
                    !Character.isWhitespace(codePoint)
                ) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false
        }
    }

}