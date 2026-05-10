package com.padabajka.dating.core.platform.data.geo

interface PlatformLocationProvider {

    suspend fun cachedLocation(): PlatformLocation?

    suspend fun requestCurrentLocation(): PlatformLocation?
}
