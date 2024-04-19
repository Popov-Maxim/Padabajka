package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.swiper.Card
import com.fp.padabajka.core.repository.api.model.swiper.Reaction
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences

interface CardRepository {
    suspend fun getNextCard(searchPreferences: SearchPreferences): Card
    suspend fun react(reaction: Reaction)
}
