package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd

interface NativeAdRepository {
    suspend fun loadNextAd(): PlatformNativeAd?
}
