@file:OptIn(ExperimentalForeignApi::class)

package com.padabajka.dating.shared.firebase.perf

import com.padabajka.dating.shared.firebase.perf.metrics.HttpMetric
import kotlinx.cinterop.ExperimentalForeignApi
import cocoapods.FirebasePerformance.FIRHTTPMetric as FirebaseHttpMetric

class IosHttpMetric(
    private val httpMetric: FirebaseHttpMetric
) : HttpMetric {
    override fun setHttpResponseCode(code: Int) {
        httpMetric.setResponseCode(code.toLong())
    }

    override fun start() {
        httpMetric.start()
    }

    override fun stop() {
        httpMetric.stop()
    }
}
