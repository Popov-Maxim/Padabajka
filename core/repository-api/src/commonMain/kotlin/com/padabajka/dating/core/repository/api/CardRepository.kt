package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.Card
import com.padabajka.dating.core.repository.api.model.swiper.Reaction
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

interface CardRepository {
    suspend fun getNextCard(searchPreferences: SearchPreferences): Card
    suspend fun react(reaction: Reaction)
}
