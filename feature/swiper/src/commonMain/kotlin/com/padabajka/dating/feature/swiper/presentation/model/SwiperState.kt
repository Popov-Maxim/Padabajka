package com.padabajka.dating.feature.swiper.presentation.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.swiper.AdCard
import com.padabajka.dating.core.repository.api.model.swiper.Card
import com.padabajka.dating.core.repository.api.model.swiper.EmptyCard
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonCard
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.presentation.screen.CardDeck
import kotlinx.collections.immutable.PersistentList

@Immutable
data class SwiperState(
    val cardDeck: CardDeck,
    val searchPreferences: SearchPreferencesItem
) : State

sealed interface SearchPreferencesItem {
    data class Success(
        val ageRange: AgeRange,
        val lookingGenders: PersistentList<Gender>,
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
    val details: PersistentList<Detail>,
    val mainAchievement: Achievement?,
    val achievements: PersistentList<Achievement>
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
            profile.images,
            profile.aboutMe,
            profile.details,
            profile.mainAchievement,
            profile.achievements
        )
    }
}

fun SearchPreferences.toUISearchPreferences(): SearchPreferencesItem {
    return this.run {
        SearchPreferencesItem.Success(
            ageRange,
            lookingGenders,
            distanceInKm,
        )
    }
}

fun SearchPreferencesItem.Success.toSearchPreferences(): SearchPreferences {
    return this.run {
        SearchPreferences(
            ageRange,
            lookingGenders,
            distanceInKm
        )
    }
}
