package com.fp.padabajka.core.data.network.model

import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PersonResponse(
    val personId: PersonId,
    val firstName: String,
    val lastName: String,
    val birthday: String,
    val aboutMe: String
)

fun PersonResponse.toPerson(): Person {
    return Person(
        id = personId,
        profile = toProfile()
    )
}

fun PersonResponse.toProfile(): Profile {
    return Profile(
        firstName = firstName,
        lastName = lastName,
        birthday = LocalDate.parse(birthday),
        images = persistentListOf(),
        aboutMe = aboutMe,
        details = persistentListOf(),
        mainAchievement = null,
        achievements = persistentListOf()
    )
}
