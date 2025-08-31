package com.padabajka.dating.feature.ads.data

import com.padabajka.dating.core.repository.api.NativeAdRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.feature.ads.data.source.NativeAdRemoteDataSource

internal class NativeAdRepositoryImpl(
    private val profileRepository: ProfileRepository,
    private val adRemoteDataSource: NativeAdRemoteDataSource
) : NativeAdRepository {

    override suspend fun loadNextAd(): PlatformNativeAd? {
        val profile = profileRepository.profileValue
        return adRemoteDataSource.loadAd(profile)
    }
}
