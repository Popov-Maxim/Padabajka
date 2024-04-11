package com.fp.padabajka.feature.swiper.domain

import com.fp.padabajka.core.repository.api.CardRepository
import com.fp.padabajka.core.repository.api.model.swiper.Card
import kotlinx.coroutines.channels.Channel

class CardChannelProvider(private val cardRepository: CardRepository) {
    val cardChannel: Channel<Card>
        get() = cardRepository.cardChanel
}
