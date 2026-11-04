package com.padabajka.dating.feature.swiper.data.search.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchPrefLocalCacheDto(
    val preferences: SearchPrefLocalDto? = null,
)
