package com.fp.padabajka.core.repository.api.model.ads

import android.view.ViewGroup

actual interface PlatformNativeAd {
    fun bind(container: ViewGroup)
}
