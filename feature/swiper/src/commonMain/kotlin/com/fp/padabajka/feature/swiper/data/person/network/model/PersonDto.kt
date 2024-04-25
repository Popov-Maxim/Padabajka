package com.fp.padabajka.feature.swiper.data.person.network.model

import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

data class PersonDto(
    val personId: PersonId,
    val firstName: String,
    val lastName: String,
    val birthday: String,
    val aboutMe: String
)

fun PersonDto.toPerson(): Person {
    return Person(
        personId,
        Profile(
            firstName = firstName,
            lastName = lastName,
            birthday = LocalDate.parse(birthday),
            images = persistentListOf(),
            aboutMe = aboutMe,
            details = persistentListOf(),
            mainAchievement = null,
            achievements = persistentListOf()
        )
    )
}
