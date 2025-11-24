package com.padabajka.dating.feature.reaction.screen.presentation.model

import com.padabajka.dating.core.repository.api.model.swiper.PersonId

sealed interface LikesMeEvent {
    data class Like(val personId: PersonId) : LikesMeEvent
    data class Dislike(val personId: PersonId) : LikesMeEvent
}
