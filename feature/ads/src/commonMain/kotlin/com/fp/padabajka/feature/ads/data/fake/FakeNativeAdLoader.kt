package com.fp.padabajka.feature.ads.data.fake

import com.fp.padabajka.core.data.NativeAdLoader

class FakeNativeAdLoader : NativeAdLoader {
    override fun setListener(listener: NativeAdLoader.Listener?) {
        // do nothing
    }

    override fun loadAd(configuration: NativeAdLoader.Configuration) {
        // do nothing
    }
}
