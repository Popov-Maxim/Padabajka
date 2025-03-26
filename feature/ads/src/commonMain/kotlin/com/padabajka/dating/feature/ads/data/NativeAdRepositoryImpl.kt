package com.padabajka.dating.feature.ads.data

import com.padabajka.dating.core.repository.api.NativeAdRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.ads.data.source.NativeAdRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

internal class NativeAdRepositoryImpl(
    scope: CoroutineScope,
    profileRepository: ProfileRepository,
    private val adRemoteDataSource: NativeAdRemoteDataSource
) : NativeAdRepository {

    private val profile: Profile? by profileRepository.profile
        .stateIn(scope, SharingStarted.Eagerly, null)::value

    override suspend fun loadNextAd(): PlatformNativeAd? {
        return adRemoteDataSource.loadAd(profile)
    }
}
