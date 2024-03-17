package com.fp.padabajka.feature.swiper.domain

import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.Reaction

class ReactToPersonUseCase(private val personRepository: PersonRepository) {
    suspend operator fun invoke(reaction: Reaction) {
        personRepository.react(reaction)
    }
}
