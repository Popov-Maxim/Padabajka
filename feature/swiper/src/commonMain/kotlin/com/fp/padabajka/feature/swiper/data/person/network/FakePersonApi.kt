package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.person.network.model.PersonDto
import kotlinx.datetime.LocalDate

class FakePersonApi : PersonApi {

    private var index = 0

    override suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonDto> {
        return List(count) {
            createPersonDto(index).also {
                index++
            }
        }
    }

    private fun createPersonDto(index: Int): PersonDto {
        return PersonDto(
            PersonId("$index"),
            "firstName $index",
            "lastName $index",
            BIRTHDAY,
            "aboutMe $index"
        )
    }

    companion object {
        private val BIRTHDAY = LocalDate(1990, 1, 1).toString()
    }
}
