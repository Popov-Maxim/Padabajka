package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PersonResponse(
    val personId: PersonId,
    val name: String,
    val birthday: String,
    val aboutMe: String,
    val lookingFor: LookingForDataDto? = null,
    val images: List<ImageDto> = listOf(),
    val lifestyles: List<LifestyleDto> = listOf(),
    val details: List<DetailDto> = listOf()
)

fun PersonResponse.toPerson(): Person {
    return Person(
        id = personId,
        profile = toProfile()
    )
}

fun PersonResponse.toProfile(): Profile {
    return Profile(
        name = name,
        birthday = LocalDate.parse(birthday),
        images = images.map { it.toImage() }.toPersistentList(),
        aboutMe = aboutMe,
        lookingFor = lookingFor?.toLookingForData() ?: LookingForData.default,
        details = details.map { it.toDomain() },
        lifestyles = lifestyles.map { it.toDomain() },
        mainAchievement = null,
        achievements = persistentListOf(),
    )
}
