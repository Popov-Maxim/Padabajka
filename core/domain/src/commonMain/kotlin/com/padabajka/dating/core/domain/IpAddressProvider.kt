package com.padabajka.dating.core.domain

expect class IpAddressProvider {
    fun getIpAddress(): String?
}
