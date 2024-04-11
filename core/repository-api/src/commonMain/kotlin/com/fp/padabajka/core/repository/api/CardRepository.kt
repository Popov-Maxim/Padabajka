package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.swiper.Card
import com.fp.padabajka.core.repository.api.model.swiper.Reaction
import kotlinx.coroutines.channels.Channel

interface CardRepository {
    val cardChanel: Channel<Card>
    suspend fun react(reaction: Reaction)
}
