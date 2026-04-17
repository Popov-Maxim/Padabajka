package com.padabajka.dating.shared.firebase.perf

import com.padabajka.dating.shared.firebase.perf.metrics.HttpMethod
import com.padabajka.dating.shared.firebase.perf.metrics.HttpMetric

interface FirebasePerf {
    fun newHttpMetric(url: String, httpMethod: HttpMethod): HttpMetric
}
