package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.coroutines.flow.Flow

expect class NativeAdRemoteDataStore {
    val ads: Flow<PlatformNativeAd>
    suspend fun loadAd(profile: Profile?)
}
