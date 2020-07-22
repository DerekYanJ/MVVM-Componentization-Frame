package com.yqy.common.third.wechat.bean

/**
 * @desc
 * @author derekyan
 * @date 2020/7/8
 */
class AccessTokenBean {
    var access_token: String = ""
    var expires_in = 0
    var refresh_token = ""
    var openid = ""
    var scope = ""
    var unionid = ""

    var errcode = 0
    var errmsg = ""
}