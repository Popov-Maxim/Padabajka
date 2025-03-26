package com.padabajka.dating.feature.swiper.domain

import com.padabajka.dating.core.repository.api.CardRepository
import com.padabajka.dating.core.repository.api.model.swiper.Reaction

class ReactToCardUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(reaction: Reaction) {
        cardRepository.react(reaction)
    }
}
