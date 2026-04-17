package com.padabajka.dating.shared.firebase.perf

import com.google.firebase.perf.ktx.performance

actual val Firebase.perf: FirebasePerf by lazy {
    AndroidFirebasePerf(com.google.firebase.ktx.Firebase.performance)
}
