package com.fp.padabajka.feature.swiper.domain

import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.ReactionResponse
import kotlinx.coroutines.flow.Flow

class ReactionResponseProvider(private val personRepository: PersonRepository) {
    val reactionResponse: Flow<ReactionResponse>
        get() = personRepository.reactionResponse
}
