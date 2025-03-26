package com.padabajka.dating.feature.ads.data.fake

import com.padabajka.dating.core.data.NativeAdLoader

class FakeNativeAdLoader : NativeAdLoader {
    override fun setListener(listener: NativeAdLoader.Listener?) {
        // do nothing
    }

    override fun loadAd(configuration: NativeAdLoader.Configuration) {
        // do nothing
    }
}
