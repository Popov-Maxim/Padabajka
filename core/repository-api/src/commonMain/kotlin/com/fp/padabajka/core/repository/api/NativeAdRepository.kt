package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import kotlinx.coroutines.flow.Flow

interface NativeAdRepository {
    val ads: Flow<PlatformNativeAd>
    suspend fun loadNextAd()
}
