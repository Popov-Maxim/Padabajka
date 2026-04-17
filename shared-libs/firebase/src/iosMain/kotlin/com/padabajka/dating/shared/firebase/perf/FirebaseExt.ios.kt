package com.padabajka.dating.shared.firebase.perf

actual val Firebase.perf: FirebasePerf by lazy {
    IosFirebasePerf()
}
