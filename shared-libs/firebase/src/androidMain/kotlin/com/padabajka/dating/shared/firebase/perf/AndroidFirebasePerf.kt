package com.padabajka.dating.shared.firebase.perf

import com.google.firebase.perf.FirebasePerformance
import com.padabajka.dating.shared.firebase.perf.metrics.AndroidHttpMetric
import com.padabajka.dating.shared.firebase.perf.metrics.HttpMethod
import com.padabajka.dating.shared.firebase.perf.metrics.HttpMetric

class AndroidFirebasePerf(
    private val firebasePerformance: FirebasePerformance,
) : FirebasePerf {
    override fun newHttpMetric(url: String, httpMethod: HttpMethod): HttpMetric {
        val httpMethod = firebasePerformance.newHttpMetric(url, httpMethod.raw)

        return AndroidHttpMetric(httpMethod)
    }
}
