package com.yqy.net

/**
 * 网络请求返回基类
 * Created by zsl on 2020/3/3.
 */
open class AbsResult<T> {
    var status: String = ""
    var msg: String = ""
    var data: T? = null
}
