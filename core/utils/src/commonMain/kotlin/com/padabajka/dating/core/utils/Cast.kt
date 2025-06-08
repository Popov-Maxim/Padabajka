package com.padabajka.dating.core.utils

inline fun <reified T> Any.safeCast(): T? = this as? T
