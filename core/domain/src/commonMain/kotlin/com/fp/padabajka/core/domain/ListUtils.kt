package com.fp.padabajka.core.domain

fun <T> List<T>.replaced(oldValue: T, newValue: T): List<T> = map {
    if (it == oldValue) newValue else it
}
