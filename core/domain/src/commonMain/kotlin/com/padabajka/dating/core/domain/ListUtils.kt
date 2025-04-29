package com.padabajka.dating.core.domain

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

fun <T> List<T>.replaced(oldValue: T, newValue: T): List<T> = map {
    if (it == oldValue) newValue else it
}

fun <T> PersistentList<T>.replaced(oldValue: T, newValue: T): PersistentList<T> =
    (this as List<T>).replaced(oldValue, newValue).toPersistentList()

fun <T> List<T>.indexOf(element: T, fromIndex: Int = 0): Int {
    return this.asSequence().drop(fromIndex).indexOf(element) + fromIndex
}
