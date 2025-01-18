package com.fp.padabajka.core.domain

import android.content.Context
import android.net.wifi.WifiManager
import java.net.InetAddress
import java.nio.ByteBuffer

actual class IpAddressProvider(private val context: Context) {
    actual fun getIpAddress(): String? {
        val wifiManager = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress

        return runCatching {
            val bytes = ByteBuffer.allocate(CAPACITY).putInt(ipAddress).array()
            InetAddress.getByAddress(bytes.reversedArray()).hostAddress
        }.getOrNull()
    }

    companion object {
        private const val CAPACITY = 4
    }
}
