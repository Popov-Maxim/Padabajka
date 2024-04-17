package com.fp.padabajka.core.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface MutableAtomic<T> : Atomic<T> {
    suspend fun update(update: suspend T.() -> T)
}

private class MutableAtomicImpl<T>(
    private var value: T,
    private val mutex: Mutex
) : MutableAtomic<T> {
    override suspend operator fun <R> invoke(action: suspend T.() -> R): R {
        mutex.withLock {
            return value.action()
        }
    }

    override suspend fun update(update: suspend T.() -> T) {
        mutex.withLock {
            value = value.update()
        }
    }
}

fun <T> mutableAtomic(value: T, mutex: Mutex = Mutex()): MutableAtomic<T> =
    MutableAtomicImpl(value, mutex)
