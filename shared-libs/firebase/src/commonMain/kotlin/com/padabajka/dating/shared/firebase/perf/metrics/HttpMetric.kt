package com.padabajka.dating.shared.firebase.perf.metrics

interface HttpMetric {
    fun setHttpResponseCode(code: Int)
    fun start()
    fun stop()
}
