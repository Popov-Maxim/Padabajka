package com.fp.padabajka.core.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Atomic<T>(
    private val value: T,
    private val mutex: Mutex = Mutex()
) {
    suspend operator fun <R> invoke(action: T.() -> R): R {
        mutex.withLock {
            return value.action()
        }
    }
}

inline fun <T> atomic(value: T): Atomic<T> = Atomic(value)
