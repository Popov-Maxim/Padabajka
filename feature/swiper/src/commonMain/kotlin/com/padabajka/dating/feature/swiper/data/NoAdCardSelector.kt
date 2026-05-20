package com.padabajka.dating.feature.swiper.data

class NoAdCardSelector : CardSelector {
    override fun nextType(): CardSelector.Type {
        return CardSelector.Type.Ad // TODO(P0)
    }

    override fun add(type: CardSelector.Type) = Unit
}
