package com.fp.padabajka.core.repository.api.model.ads

import android.view.View

actual interface PlatformNativeAd {
    fun bind(container: View)
}
