package com.fp.padabajka.core.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface Atomic<T> {
    suspend operator fun <R> invoke(action: suspend T.() -> R): R
}

private class AtomicImpl<T>(
    private val value: T,
    private val mutex: Mutex
) : Atomic<T> {
    override suspend operator fun <R> invoke(action: suspend T.() -> R): R {
        mutex.withLock {
            return value.action()
        }
    }
}

fun <T> atomic(value: T, mutex: Mutex = Mutex()): Atomic<T> = AtomicImpl(value, mutex)
