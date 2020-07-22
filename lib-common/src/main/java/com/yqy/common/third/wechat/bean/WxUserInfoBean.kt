package com.yqy.common.third.wechat.bean

/**
 * @desc
 * @author derekyan
 * @date 2020/7/8
 */
class WxUserInfoBean {
    var openid = ""
    var unionid = ""

    var nickname = ""
    var headimgurl = ""
    var sex = -1            // 1 为男性，2 为女性
    var language = ""
    var city = ""
    var province = ""
    var country = ""

    var errcode = 0
    var errmsg = ""
}