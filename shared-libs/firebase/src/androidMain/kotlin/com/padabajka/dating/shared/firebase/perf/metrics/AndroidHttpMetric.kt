package com.padabajka.dating.shared.firebase.perf.metrics

import com.google.firebase.perf.metrics.HttpMetric as FirebaseHttpMetric

class AndroidHttpMetric(
    private val httpMetric: FirebaseHttpMetric
) : HttpMetric {
    override fun setHttpResponseCode(code: Int) {
        httpMetric.setHttpResponseCode(code)
    }

    override fun start() {
        httpMetric.start()
    }

    override fun stop() {
        httpMetric.stop()
    }
}
