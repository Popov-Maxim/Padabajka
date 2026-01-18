package com.padabajka.dating.feature.swiper.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LanguagesAsset
import com.padabajka.dating.core.repository.api.model.profile.Lifestyle
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.core.repository.api.model.swiper.AdCard
import com.padabajka.dating.core.repository.api.model.swiper.Card
import com.padabajka.dating.core.repository.api.model.swiper.EmptyCard
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonCard
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import com.padabajka.dating.feature.swiper.presentation.screen.CardDeck
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class SwiperState(
    val cardDeck: CardDeck,
    val searchPreferences: SearchPreferencesItem
) : State

sealed interface SearchPreferencesItem {
    data class Success(
        val ageRange: AgeRange,
        val lookingGender: GenderUI,
        val distanceInKm: Int,
    ) : SearchPreferencesItem

    data object Loading : SearchPreferencesItem
}

@Immutable
sealed interface CardItem

@Immutable
data object LoadingItem : CardItem

@Immutable
data object EmptyCardItem : CardItem

@Immutable
data class NativeAdItem(
    val platformNativeAd: PlatformNativeAd
) : CardItem

@Immutable
data class PersonItem(
    val id: PersonId,
    val name: String,
    val age: Age,
    val images: PersistentList<Image>,
    val aboutMe: String,
    val lookingFor: LookingForData,
    val details: PersistentList<Detail>,
    val lifestyles: PersistentList<Lifestyle>,
    val interests: PersistentList<Text>,
    val languages: LanguagesAsset,
    val mainAchievement: Achievement?,
    val achievements: PersistentList<Achievement>,
) : CardItem

fun Card.toUICardItem(): CardItem {
    return when (this) {
        is AdCard -> NativeAdItem(this.platformNativeAd)
        EmptyCard -> EmptyCardItem
        is PersonCard -> this.person.toUIPerson()
    }
}

fun Person.toUIPerson(): PersonItem {
    return this.run {
        PersonItem(
            id,
            profile.name,
            profile.age,
            profile.images.toPersistentList(),
            profile.aboutMe,
            profile.lookingFor,
            profile.details.toPersistentList(),
            profile.lifestyles.toPersistentList(),
            profile.interests.toPersistentList(),
            profile.languagesAsset,
            profile.mainAchievement,
            profile.achievements.toPersistentList()
        )
    }
}

fun SearchPreferences.toUISearchPreferences(): SearchPreferencesItem {
    val lookingGender = if (lookingGenders.size == 1) {
        lookingGenders.first()
    } else {
        Gender.Everyone
    }
    return this.run {
        SearchPreferencesItem.Success(
            ageRange,
            lookingGender.toGenderUI(),
            distanceInKm,
        )
    }
}

fun SearchPreferencesItem.Success.toSearchPreferences(): SearchPreferences {
    val lookingGenders = when (lookingGender) {
        GenderUI.Male,
        GenderUI.Female -> listOf(lookingGender.toGender())

        GenderUI.Everyone -> Gender.entries.toList()
    }
    return this.run {
        SearchPreferences(
            ageRange,
            lookingGenders,
            distanceInKm
        )
    }
}

@Stable
fun PersonItem.toPersonView(): ProfileViewUIItem {
    return ProfileViewUIItem(
        name = name,
        age = age,
        images = images,
        aboutMe = aboutMe,
        lookingFor = lookingFor,
        details = details,
        lifestyle = lifestyles,
        interests = interests,
        languages = languages
    )
}
