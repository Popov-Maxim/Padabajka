package com.fp.padabajka.core.domain

fun interface Factory<T> {
    fun get(): T
}
