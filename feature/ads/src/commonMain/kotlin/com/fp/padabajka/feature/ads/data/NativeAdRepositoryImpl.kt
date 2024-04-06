package com.fp.padabajka.feature.ads.data

import com.fp.padabajka.core.repository.api.NativeAdRepository
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.ads.data.source.NativeAdRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

internal class NativeAdRepositoryImpl(
    scope: CoroutineScope,
    repository: ProfileRepository,
    private val adRemoteDataSource: NativeAdRemoteDataSource
) : NativeAdRepository {

    private val profile: Profile? by repository.profile
        .stateIn(scope, SharingStarted.Eagerly, null)::value

    override suspend fun loadNextAd(): PlatformNativeAd {
        return adRemoteDataSource.loadAd(profile)
    }
}
