package com.padabajka.dating.core.domain

fun <K, V> mapOfNotNull(vararg pairs: Pair<K, V?>): Map<K, V> {
    return buildMap {
        pairs.onEach {
            putOptionalValue(it)
        }
    }
}

private fun <K, V> MutableMap<K, V>.putOptionalValue(pair: Pair<K, V?>) {
    val (key, value) = pair
    if (value != null) {
        put(key, value)
    }
}
