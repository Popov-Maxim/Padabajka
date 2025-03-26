package com.padabajka.dating.feature.ads.data.source

import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Profile

internal interface NativeAdRemoteDataSource {
    suspend fun loadAd(profile: Profile?): PlatformNativeAd
}
