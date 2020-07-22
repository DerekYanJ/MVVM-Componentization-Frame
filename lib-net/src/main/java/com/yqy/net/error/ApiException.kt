package com.yqy.net.error

import java.lang.RuntimeException

/**
 * @desc
 * @author derekyan
 * @date 2020/5/18
 */
class ApiException( public var code: String = "0",
                    public var msg: String = ""): RuntimeException() {

}