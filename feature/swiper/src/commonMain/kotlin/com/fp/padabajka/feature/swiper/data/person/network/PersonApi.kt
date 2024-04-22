package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.feature.swiper.data.person.network.model.PersonDto

interface PersonApi {
    class Configuration
    suspend fun getPersons(count: Int, loaded: List<PersonId>, configuration: Configuration): List<PersonDto>
}
