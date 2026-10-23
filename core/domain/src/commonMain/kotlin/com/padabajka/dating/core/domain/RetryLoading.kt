package com.padabajka.dating.core.domain

import com.padabajka.dating.core.repository.api.exception.UserException
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlin.time.Duration.Companion.milliseconds

/** Emit fallback only if loading fails before any value; later failures keep the latest value. */
fun <T> Flow<T>.retryLoading(fallback: T, maxRetries: Long = DEFAULT_MAX_RETRIES): Flow<T> = flow {
    var hasValue = false
    emitAll(
        onEach { hasValue = true }
            .catch { cause ->
                if (cause is CancellationException || cause is UserException) throw cause

                if (!hasValue) {
                    emit(fallback)
                    hasValue = true
                }
                throw cause
            }
            .retryLoading(maxRetries = maxRetries)
    )
}

/** Retry ordinary errors up to [maxRetries] times, then complete; auth errors and cancellation propagate. */
fun <T> Flow<T>.retryLoading(maxRetries: Long = DEFAULT_MAX_RETRIES): Flow<T> {
    require(maxRetries >= 0) { "maxRetries must not be negative" }

    return retryWhen { cause, attempt ->
        if (cause is CancellationException || cause is UserException) return@retryWhen false

        if (attempt == 0L) Firebase.crashlytics.recordException(cause)

        val shouldRetry = attempt < maxRetries
        if (shouldRetry) {
            delay(LOADING_RETRY_DELAY_MS.milliseconds)
        }
        shouldRetry
    }.catch { cause ->
        if (cause is CancellationException || cause is UserException) throw cause
    }
}

private const val DEFAULT_MAX_RETRIES = 3L
private const val LOADING_RETRY_DELAY_MS = 5_000L
