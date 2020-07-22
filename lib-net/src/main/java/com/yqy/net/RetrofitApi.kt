package com.yqy.net

import com.yqy.net.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @desc
 * @author derekyan
 * @date 2020/5/15
 */
abstract class RetrofitApi {
    private val retrofitHashMap: HashMap<String, Retrofit> =
        HashMap<String, Retrofit>()

    val timeOut = 10L


    /**
     * 通过 mBaseUrl 和 service 来获取对应的 Retrofit 实例
     */
    fun getRetrofit(mBaseUrl: String, service: Class<*>): Retrofit {
        if (retrofitHashMap[mBaseUrl + service.name] != null) {
            return retrofitHashMap[mBaseUrl + service.name]!!
        }
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(mBaseUrl)
        retrofitBuilder.client(getOkHttpClient())
        setRetrofitBuilderFactory(retrofitBuilder)
        val retrofit = retrofitBuilder.build()
        // 存储 retrofit 实例
        retrofitHashMap[mBaseUrl + service.name] = retrofit
        return retrofit
    }

    /**
     * 配置 Retrofit Factory
     */
    open fun setRetrofitBuilderFactory(retrofitBuilder: Retrofit.Builder) {
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    /**
     *  获取 okHttpClient
     */
    open fun getOkHttpClient(): OkHttpClient {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustManagers: Array<TrustManager> = arrayOf(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManagers, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            // OkHttp
            val builder = OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(LoggingInterceptor())
                .dns(ConfigDns())
            builder.sslSocketFactory(sslSocketFactory, trustManagers[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}