package com.fp.padabajka.core.domain

expect class IpAddressProvider {
    fun getIpAddress(): String?
}
