package com.fp.padabajka.feature.swiper.domain

import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.Person
import kotlinx.coroutines.channels.Channel

class PersonChannelProvider(private val personRepository: PersonRepository) {
    val personChannel: Channel<Person>
        get() = personRepository.personChannel
}
