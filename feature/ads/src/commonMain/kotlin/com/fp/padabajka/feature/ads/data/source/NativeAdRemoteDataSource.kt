package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Profile

internal interface NativeAdRemoteDataSource {
    suspend fun loadAd(profile: Profile?): PlatformNativeAd
}
