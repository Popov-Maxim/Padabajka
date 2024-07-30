package com.fp.padabajka.feature.swiper.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.core.repository.api.model.profile.AgeRange
import com.fp.padabajka.core.repository.api.model.profile.Detail
import com.fp.padabajka.core.repository.api.model.profile.Gender
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.swiper.AdCard
import com.fp.padabajka.core.repository.api.model.swiper.Card
import com.fp.padabajka.core.repository.api.model.swiper.EmptyCard
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonCard
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.presentation.screen.CardDeck
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.LocalDate

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
        val showReset: Boolean = false
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
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
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
            profile.firstName,
            profile.lastName,
            profile.birthday,
            profile.images,
            profile.aboutMe,
            profile.details,
            profile.mainAchievement,
            profile.achievements
        )
    }
}

fun SearchPreferences.toUISearchPreferences(showReset: Boolean = false): SearchPreferencesItem {
    return this.run {
        SearchPreferencesItem.Success(
            ageRange,
            lookingGenders,
            distanceInKm,
            showReset
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
