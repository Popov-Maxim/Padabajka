package com.fp.padabajka.feature.swiper.domain

import com.fp.padabajka.core.repository.api.CardRepository
import com.fp.padabajka.core.repository.api.model.swiper.Reaction

class ReactToCardUseCase(private val cardRepository: CardRepository) {
    suspend operator fun invoke(reaction: Reaction) {
        cardRepository.react(reaction)
    }
}
