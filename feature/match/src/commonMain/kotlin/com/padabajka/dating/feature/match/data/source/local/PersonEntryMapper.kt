package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.person.entry.PersonEntry
import com.padabajka.dating.core.data.network.model.toDomain
import com.padabajka.dating.core.data.network.model.toDto
import com.padabajka.dating.core.data.network.model.toLookingForData
import com.padabajka.dating.core.data.network.model.toLookingForDataDto
import com.padabajka.dating.core.data.network.model.toText
import com.padabajka.dating.core.data.network.model.toTextDto
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

fun Person.toEntry(): PersonEntry {
    return PersonEntry(
        id = id.raw,
        name = profile.name,
        birthday = profile.birthday,
        images = profile.images.map { (it as Image.Url).value },
        aboutMe = profile.aboutMe,
        lookingFor = profile.lookingFor.toLookingForDataDto(),
        details = profile.details.map { it.toDto() },
        lifestyles = profile.lifestyles.map { it.toDto() },
        interests = profile.interests.map { it.toTextDto() },
        languagesAsset = profile.languagesAsset.toDto(),
    )
}

fun PersonEntry.toPerson(): Person {
    return Person(
        id = PersonId(id),
        profile = toProfile()
    )
}

fun PersonEntry.toProfile(): Profile {
    return Profile(
        name = name,
        birthday = birthday,
        images = images.map { Image.Url(it) }.toPersistentList(),
        aboutMe = aboutMe,
        lookingFor = lookingFor.toLookingForData(),
        details = details.map { it.toDomain() },
        lifestyles = lifestyles.map { it.toDomain() },
        interests = interests.map { it.toText() },
        languagesAsset = languagesAsset.toDomain(),
        mainAchievement = null,
        achievements = persistentListOf(),
        isFrozen = false
    )
}
