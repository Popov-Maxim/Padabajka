package com.padabajka.dating.core.domain

inline fun <K, V> mapOfNotNull(vararg pairs: Pair<K, V?>): Map<K, V> {
    return pairs.filterValueNotNull().toMap()
}

fun <K, V> Array<out Pair<K, V?>>.filterValueNotNull(): Array<Pair<K, V>> {
    return mapNotNull { (key, value) -> value?.let { key to it } }.toTypedArray()
}
