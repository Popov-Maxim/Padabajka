package com.fp.padabajka.feature.swiper.data

class NoAdCardSelector : CardSelector {
    override fun nextType(): CardSelector.Type {
        return CardSelector.Type.Person
    }

    override fun add(type: CardSelector.Type) = Unit
}
