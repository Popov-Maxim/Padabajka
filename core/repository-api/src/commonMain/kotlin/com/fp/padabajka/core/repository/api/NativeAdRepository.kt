package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd

interface NativeAdRepository {
    suspend fun loadNextAd(): PlatformNativeAd?
}
