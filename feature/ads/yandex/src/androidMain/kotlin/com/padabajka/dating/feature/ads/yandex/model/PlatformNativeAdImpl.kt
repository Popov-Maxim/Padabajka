package com.padabajka.dating.feature.ads.yandex.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.feature.ads.yandex.R
import com.yandex.mobile.ads.nativeads.MediaView
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdView
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder

class PlatformNativeAdImpl(
    private val nativeAd: NativeAd
) : PlatformNativeAd {
    override fun bind(container: ViewGroup) {
        val binder = createBinder(container)
        container.removeAllViews()
        container.addView(binder.nativeAdView)

        nativeAd.bindNativeAd(binder)
    }

    private fun createBinder(container: ViewGroup): NativeAdViewBinder {
        val root = createNativeAdView(container.context)

        val icon = root.findViewById<ImageView>(R.id.icon)
        val domain = root.findViewById<TextView>(R.id.domain)
        val sponsored = root.findViewById<TextView>(R.id.sponsored)
        val feedback = root.findViewById<ImageView>(R.id.feedback)
        val title = root.findViewById<TextView>(R.id.title)
        val body = root.findViewById<TextView>(R.id.body)
        val mediaView = root.findViewById<MediaView>(R.id.mediaView)
        val callToAction = root.findViewById<TextView>(R.id.call_to_action)
        val warning = root.findViewById<TextView>(R.id.warning)

        return NativeAdViewBinder.Builder(root)
            .setIconView(icon)
            .setFaviconView(icon)
            .setDomainView(domain)
            .setSponsoredView(sponsored)
            .setFeedbackView(feedback)
            .setTitleView(title)
            .setBodyView(body)
            .setMediaView(mediaView)
            .setCallToActionView(callToAction)
            .setWarningView(warning)
            .build()
    }

    private fun createNativeAdView(context: Context): NativeAdView {
        val inflater = LayoutInflater.from(context)
        val myLayoutView: NativeAdView = inflater.inflate(R.layout.ad_card, null) as NativeAdView
        return myLayoutView
    }
}
