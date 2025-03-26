package com.padabajka.dating.feature.swiper.data

interface CardSelector {
    sealed interface Type {
        data object Ad : Type
        data object Person : Type
    }
    fun nextType(): Type
    fun add(type: Type)
}
