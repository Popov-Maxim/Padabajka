package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.feature.swiper.data.person.network.model.PersonDto

class FakePersonApi : PersonApi {

    private var index = 0

    override suspend fun getPersons(
        count: Int,
        configuration: PersonApi.Configuration
    ): List<PersonDto> {
        return List(count) {
            createPersonDto(index).apply {
                index++
            }
        }
    }

    @Suppress("UnusedParameter")
    private fun createPersonDto(index: Int): PersonDto {
        return object : PersonDto {}
    }
}
