package com.yqy.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.yqy.net.error.ApiException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * 异常信息
 * 网路异常 & 约定异常
 */
object ExceptionHandle {
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504
    fun handleException(e: Throwable?): ApiException {
        val ex: ApiException
        return if (e is HttpException) {
            ex = ApiException(
                ERROR.HTTP_ERROR.toString(),
                ""
            )
            when (e.code()) {
                UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE -> ex.msg =
                    "网络错误"
                else -> ex.msg = "网络错误"
            }
            ex
        } else if (e is ServerException) {
            ApiException(e.code.toString(), e.message!!)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {
            ApiException(
                ERROR.PARSE_ERROR.toString(),
                "解析错误"
            )
        } else if (e is ConnectException) {
            ApiException(
                ERROR.NETWORD_ERROR.toString(),
                "连接失败"
            )
        } else if (e is SSLHandshakeException) {
            ApiException(
                ERROR.SSL_ERROR.toString(),
                "证书验证失败"
            )
        } else if (e is ConnectTimeoutException) {
            ApiException(
                ERROR.TIMEOUT_ERROR.toString(),
                "连接超时"
            )
        } else if (e is SocketTimeoutException) {
            ApiException(
                ERROR.TIMEOUT_ERROR.toString(),
                "连接超时"
            )
        } else {
            ApiException(
                ERROR.UNKNOWN.toString(),
                "未知错误"
            )
        }
    }

    fun handleException(code: String, msg: String): ApiException {
        when (code) {
            EnumError.FORBIDDEN_NUMBER.status -> {
                return ApiException(
                    EnumError.FORBIDDEN_NUMBER.status,
                    EnumError.FORBIDDEN_NUMBER.msg
                )
            }
        }
        return ApiException(code, msg)
    }

    enum class EnumError constructor(var status: String, var msg: String) {
        FORBIDDEN_NUMBER("1001", "您的账号涉嫌违规已被封禁\\n如需帮助请加QQ\\nQQ号码:3604412227"),
    }

    /**
     * 约定异常
     */
    object ERROR {

        /**
         * 未知错误
         */
        const val UNKNOWN = 10000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = 10001

        /**
         * 网络错误
         */
        const val NETWORD_ERROR = 10002

        /**
         * 协议出错
         */
        const val HTTP_ERROR = 10003

        /**
         * 证书出错
         */
        const val SSL_ERROR = 10005

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = 10006
    }

    class ServerException : RuntimeException() {
        var code = 0
        override var message: String? = null
    }
}