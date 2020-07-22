package com.yqy.common.third.wechat

import com.yqy.common.third.wechat.bean.AccessTokenBean
import com.yqy.common.third.wechat.bean.WxUserInfoBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @desc
 * @author derekyan
 * @date 2020/7/8
 */
interface WXApiService {

    @GET(WXConstants.API_ACCESS_TOKEN)
    suspend fun accessToken(
        @Query("appid") appid: String,
        @Query("secret") secret: String,
        @Query("code") code: String,
        @Query("grant_type") grant_type: String
    ): Response<AccessTokenBean>

    @GET(WXConstants.API_USER_INFO)
    suspend fun userInfo(
        @Query("access_token") access_token: String,
        @Query("openid") openid: String
    ): Response<WxUserInfoBean>
}