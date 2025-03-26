package com.padabajka.dating.core.repository.api.model.swiper

import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd

sealed interface Card

data class AdCard(val platformNativeAd: PlatformNativeAd) : Card

data class PersonCard(val person: Person) : Card

data object EmptyCard : Card
