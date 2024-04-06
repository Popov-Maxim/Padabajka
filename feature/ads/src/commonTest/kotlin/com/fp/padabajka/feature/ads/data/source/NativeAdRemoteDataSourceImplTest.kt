package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.data.MockNativeAdLoader
import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.core.repository.api.model.ads.MockPlatformNativeAd
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.testing.ObservableMutableList
import com.fp.padabajka.testing.asObservable
import com.fp.padabajka.testing.assertThrows
import kotlinx.coroutines.runBlocking
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@UsesMocks(NativeAdLoader::class, PlatformNativeAd::class, CompositeListener::class)
class NativeAdRemoteDataSourceImplTest {
    private val mocker = Mocker()
    private val nativeAdLoader: NativeAdLoader = MockNativeAdLoader(mocker)
    private val expectedPlatformNativeAd: PlatformNativeAd = MockPlatformNativeAd(mocker)
    private val compositeListener: CompositeListener = MockCompositeListener(mocker)
    private val capture = mutableListOf<NativeAdLoader.Listener>().asObservable()

    private lateinit var nativeAdRemoteDataSourceImpl: NativeAdRemoteDataSourceImpl

    @BeforeTest
    fun setUp() {
        with(mocker) {
            every { nativeAdLoader.setListener(compositeListener) } returns Unit
            every { compositeListener.addListener(isAny(capture)) } returns Unit
            every { nativeAdLoader.loadAd(isAny()) } returns Unit
        }

        nativeAdRemoteDataSourceImpl = NativeAdRemoteDataSourceImpl(
            nativeAdLoader, compositeListener
        )
    }

    @Test
    fun testLoadAdSuccess() = runBlocking {
        capture.listener = ObservableMutableList.Listener { element ->
            element.onLoaded(expectedPlatformNativeAd)
        }
        val platformNativeAd = nativeAdRemoteDataSourceImpl.loadAd(null)

        assertTrue {
            platformNativeAd == expectedPlatformNativeAd
        }
    }

    @Test
    fun testLoadAdFailed() = runBlocking {
        capture.listener = ObservableMutableList.Listener { element ->
            element.onError(ERROR_DESCRIPTION)
        }
        assertThrows(LoadErrorException(ERROR_DESCRIPTION)) {
            nativeAdRemoteDataSourceImpl.loadAd(null)
        }
    }

    companion object {
        private const val ERROR_DESCRIPTION = "error_description"
    }
}
