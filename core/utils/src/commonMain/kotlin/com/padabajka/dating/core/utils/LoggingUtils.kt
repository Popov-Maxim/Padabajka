package com.padabajka.dating.core.utils

@Suppress("NOTHING_TO_INLINE")
inline fun printlnDev(any: Any?) {
    if (isDebugBuild) {
        println(any)
    }
}
