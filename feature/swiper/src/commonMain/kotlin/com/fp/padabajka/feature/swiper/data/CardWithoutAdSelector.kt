package com.fp.padabajka.feature.swiper.data

class CardWithoutAdSelector : CardSelector {
    override fun nextType(): CardSelector.Type {
        return CardSelector.Type.Person
    }

    override fun add(type: CardSelector.Type) = Unit
}
