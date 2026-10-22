package com.padabajka.dating.shared.firebase.crashlytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlin.coroutines.cancellation.CancellationException

/** Reports non-cancellation failures to Crashlytics and preserves this result. */
fun <T> Result<T>.logFailureToCrashlytics(): Result<T> = onFailure { exception ->
    if (exception !is CancellationException) {
        Firebase.crashlytics.recordException(exception)
    }
}
