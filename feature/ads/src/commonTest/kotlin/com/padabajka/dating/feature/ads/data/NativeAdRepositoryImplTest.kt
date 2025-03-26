package com.padabajka.dating.feature.ads.data

import com.padabajka.dating.core.repository.api.MockProfileRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.ads.MockPlatformNativeAd
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.ads.data.source.MockNativeAdRemoteDataSource
import com.padabajka.dating.feature.ads.data.source.NativeAdRemoteDataSource
import com.padabajka.dating.testing.testScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@UsesMocks(ProfileRepository::class, NativeAdRemoteDataSource::class, PlatformNativeAd::class)
class NativeAdRepositoryImplTest {
    private val mocker = Mocker()
    private val scope: CoroutineScope = testScope()
    private val flow: Flow<Profile> = flow { }
    private val repository: ProfileRepository = MockProfileRepository(mocker)
    private val adRemoteDataSource: NativeAdRemoteDataSource = MockNativeAdRemoteDataSource(mocker)
    private val expectedPlatformNativeAd: PlatformNativeAd = MockPlatformNativeAd(mocker)

    private lateinit var nativeAdRepositoryImpl: NativeAdRepositoryImpl

    @BeforeTest
    fun setUp() = runBlocking {
        with(mocker) {
            every { repository.profile } returns flow
            everySuspending { adRemoteDataSource.loadAd(null) } returns expectedPlatformNativeAd
        }

        nativeAdRepositoryImpl = NativeAdRepositoryImpl(
            scope,
            repository,
            adRemoteDataSource
        )
    }

    @Test
    fun testLoadNextAd() = runBlocking {
        val platformNativeAd = nativeAdRepositoryImpl.loadNextAd()

        assertTrue { platformNativeAd == expectedPlatformNativeAd }
    }
}
