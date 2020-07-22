package com.yqy.net

import android.text.TextUtils
import okhttp3.Dns
import java.net.Inet4Address
import java.net.InetAddress

/**
 * 由于后端配有搭建IPV6地址，
 * 所以移动端需要设置IPV4优先，
 * 否则会报 Unable to resolve host "xxx.xx.cn" :No address associated with hostname
 */

class ConfigDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        if (TextUtils.isEmpty(hostname)) {
            return Dns.SYSTEM.lookup(hostname)
        } else {
            return try {
                val addressList = arrayListOf<InetAddress>()
                val addresses = InetAddress.getAllByName(hostname)
                for (net in addresses) {
                    if (net is Inet4Address) {
                        addressList.add(0, net)
                    } else {
                        addressList.add(net)
                    }
                }
                addressList
            } catch (e: Exception) {
                Dns.SYSTEM.lookup(hostname)
            }
        }
    }

}