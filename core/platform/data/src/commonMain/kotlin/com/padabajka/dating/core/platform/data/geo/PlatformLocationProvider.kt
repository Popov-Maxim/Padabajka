package com.padabajka.dating.core.platform.data.geo

interface PlatformLocationProvider {

    suspend fun getCurrentLocation(): PlatformLocation?
}
