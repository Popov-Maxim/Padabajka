package com.fp.padabajka.feature.swiper.data.person.network

import com.fp.padabajka.core.data.network.model.PersonResponse
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import kotlinx.datetime.LocalDate

class FakePersonApi : PersonApi {

    private var index = 0

    override suspend fun getPersons(
        count: Int,
        loaded: List<PersonId>,
        searchPreferences: SearchPreferences
    ): List<PersonResponse> {
        return List(count) {
            createPersonDto(index).also {
                index++
            }
        }
    }

    private fun createPersonDto(index: Int): PersonResponse {
        return PersonResponse(
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
