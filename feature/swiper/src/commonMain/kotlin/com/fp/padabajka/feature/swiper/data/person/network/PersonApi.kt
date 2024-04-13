package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.feature.swiper.data.person.network.model.PersonDto

interface PersonApi {
    class Configuration
    suspend fun getPersons(count: Int, configuration: Configuration): List<PersonDto>
}
