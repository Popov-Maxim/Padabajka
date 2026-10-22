package com.padabajka.dating.core.utils

import kotlin.coroutines.cancellation.CancellationException

/** Like [runCatching], but propagates coroutine cancellation. */
inline fun <T> runCatchingCancellable(block: () -> T): Result<T> =
    runCatching(block).onFailure { exception ->
        if (exception is CancellationException) throw exception
    }
