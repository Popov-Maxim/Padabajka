@file:OptIn(ExperimentalForeignApi::class)

package com.padabajka.dating.shared.firebase.perf

import cocoapods.FirebasePerformance.FIRHTTPMethod
import com.padabajka.dating.shared.firebase.perf.metrics.HttpMethod
import com.padabajka.dating.shared.firebase.perf.metrics.HttpMetric
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import cocoapods.FirebasePerformance.FIRHTTPMetric as FirebaseHttpMetric

class IosFirebasePerf : FirebasePerf {
    override fun newHttpMetric(url: String, httpMethod: HttpMethod): HttpMetric {
        val urlNS = NSURL.URLWithString(url)!!
        val method = httpMethod.toFIRHTTPMethod()
        val httpMethod = FirebaseHttpMetric(urlNS, method)

        return IosHttpMetric(httpMethod)
    }
}

private fun HttpMethod.toFIRHTTPMethod(): FIRHTTPMethod {
    return when (this) {
        HttpMethod.GET -> FIRHTTPMethod.FIRHTTPMethodGET
        HttpMethod.PUT -> FIRHTTPMethod.FIRHTTPMethodPUT
        HttpMethod.CONNECT -> FIRHTTPMethod.FIRHTTPMethodCONNECT
        HttpMethod.PATCH -> FIRHTTPMethod.FIRHTTPMethodPATCH
        HttpMethod.OPTIONS -> FIRHTTPMethod.FIRHTTPMethodOPTIONS
        HttpMethod.TRACE -> FIRHTTPMethod.FIRHTTPMethodTRACE
        HttpMethod.DELETE -> FIRHTTPMethod.FIRHTTPMethodDELETE
        HttpMethod.POST -> FIRHTTPMethod.FIRHTTPMethodPOST
        HttpMethod.HEAD -> FIRHTTPMethod.FIRHTTPMethodHEAD
        HttpMethod.UNKNOWN -> TODO()
    }
}
