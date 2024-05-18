package com.fp.padabajka.core.domain

import kotlin.properties.ReadOnlyProperty

fun interface Factory<T> {
    fun get(): T
}

fun <T> Factory<T>.delegate(): ReadOnlyProperty<Any?, T> =
    ReadOnlyProperty { _, _ -> get() }
