package com.fp.padabajka.core.domain

class Factory<T>(private val provider: () -> T) {
    fun get(): T = provider()
}
