package com.fp.padabajka.core.data.network.model

import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PersonResponse(
    val personId: PersonId,
    val firstName: String,
    val lastName: String,
    val birthday: String,
    val aboutMe: String,
    val images: List<ImageDto>
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
        images = images.map { it.toImage() }.toPersistentList(),
        aboutMe = aboutMe,
        details = persistentListOf(),
        mainAchievement = null,
        achievements = persistentListOf()
    )
}
