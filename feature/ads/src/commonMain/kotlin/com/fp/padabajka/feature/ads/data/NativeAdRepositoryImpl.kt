package com.fp.padabajka.feature.ads.data

import com.fp.padabajka.core.repository.api.NativeAdRepository
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.ads.data.source.NativeAdRemoteDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NativeAdRepositoryImpl(
    scope: CoroutineScope,
    private val repository: ProfileRepository,
    private val adRemoteDataStore: NativeAdRemoteDataStore
) : NativeAdRepository {

    private var profile: Profile? = null

    init {
        scope.launch {
            repository.profile.collect {
                profile = it
            }
        }
    }

    override val ads: Flow<PlatformNativeAd>
        get() = adRemoteDataStore.ads

    override suspend fun loadNextAd() {
        adRemoteDataStore.loadAd(profile)
    }
}
