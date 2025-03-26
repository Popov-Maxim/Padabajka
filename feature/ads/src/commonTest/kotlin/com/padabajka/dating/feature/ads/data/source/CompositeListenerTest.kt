package com.padabajka.dating.feature.ads.data.source

import com.padabajka.dating.core.data.MockNativeAdLoader_Listener
import com.padabajka.dating.core.data.NativeAdLoader
import com.padabajka.dating.core.repository.api.model.ads.MockPlatformNativeAd
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks
import kotlin.test.Test

@UsesMocks(NativeAdLoader.Listener::class, PlatformNativeAd::class)
class CompositeListenerTest {

    private val mocker = Mocker()
    private val platformNativeAd: PlatformNativeAd = MockPlatformNativeAd(mocker)
    private val compositeListener = CompositeListener()

    @Test
    fun testOnLoadedWithoutListener() {
        compositeListener.onLoaded(platformNativeAd)
    }

    @Test
    fun testOnLoadedWithOneListener() {
        val firstListener = MockNativeAdLoader_Listener(mocker)
        mocker.every { firstListener.onLoaded(platformNativeAd) } returns Unit
        compositeListener.addListener(firstListener)

        compositeListener.onLoaded(platformNativeAd)

        mocker.verify {
            firstListener.onLoaded(platformNativeAd)
        }
    }

    @Test
    fun testOnLoadedWithTenListener() {
        val listeners = (0 until 10).map { MockNativeAdLoader_Listener(mocker) }
        mocker.every { listeners.first().onLoaded(platformNativeAd) } returns Unit
        listeners.forEach {
            compositeListener.addListener(it)
        }

        compositeListener.onLoaded(platformNativeAd)

        mocker.verify {
            listeners.first().onLoaded(platformNativeAd)
        }
    }

    @Test
    fun testOnErrorWithoutListener() {
        compositeListener.onLoaded(platformNativeAd)
    }

    @Test
    fun testOnErrorWithOneListener() {
        val firstListener = MockNativeAdLoader_Listener(mocker)
        mocker.every { firstListener.onError(ERROR_DESCRIPTION) } returns Unit
        compositeListener.addListener(firstListener)

        compositeListener.onError(ERROR_DESCRIPTION)

        mocker.verify {
            firstListener.onError(ERROR_DESCRIPTION)
        }
    }

    @Test
    fun testOnErrorWithTenListener() {
        val listeners = (0 until 10).map { MockNativeAdLoader_Listener(mocker) }
        mocker.every { listeners.first().onError(ERROR_DESCRIPTION) } returns Unit
        listeners.forEach {
            compositeListener.addListener(it)
        }

        compositeListener.onError(ERROR_DESCRIPTION)

        mocker.verify {
            listeners.first().onError(ERROR_DESCRIPTION)
        }
    }

    companion object {
        private const val ERROR_DESCRIPTION = "error_description"
    }
}
