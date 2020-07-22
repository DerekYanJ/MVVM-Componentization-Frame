package com.yqy.common.third.wechat

import com.yqy.net.RetrofitApi
import com.yqy.net.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*


/**
 * 使用Http.api()
 * Created by zsl on 2020/3/3.
 */
class HttpWx: RetrofitApi() {

    private val mWXRetrofit: Retrofit = getRetrofit(WXConstants.BASE_URL, WXApiService::class.java)


    override fun setRetrofitBuilderFactory(retrofitBuilder: Retrofit.Builder) {
//        super.setRetrofitBuilderFactory(retrofitBuilder)
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
    }

    fun providerWxApi(): WXApiService {
        return mWXRetrofit.create(WXApiService::class.java)
    }

    companion object {
        fun api(): WXApiService {
            return Loader.request.providerWxApi()
        }
    }

    private object Loader {
        val request: HttpWx =
            HttpWx()
    }

    /** 不校验证书签名 **/
    override fun getOkHttpClient(): OkHttpClient {
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
//                .addInterceptor(HeaderInterceptor())
                .addInterceptor(LoggingInterceptor())

            builder.sslSocketFactory(sslSocketFactory, trustManagers[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}