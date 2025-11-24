package com.padabajka.dating.feature.reaction.screen.presentation.model

import androidx.compose.runtime.Stable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import com.padabajka.dating.feature.profile.presentation.model.toPersonView
import com.padabajka.dating.feature.reaction.screen.domain.ReactionsToMe
import kotlinx.collections.immutable.PersistentList

data class LikesMeState(
    val listReactions: ListReactions = ListReactions.Idle
) : State

sealed interface ListReactions {
    data object Idle : ListReactions
    data object Loading : ListReactions
    data class Error(val message: String) : ListReactions
    data class Success(
        val likes: PersistentList<ReactionUIState>
    ) : ListReactions
}

sealed interface ReactionUIState {

    @Stable
    val personId: PersonId

    @Stable
    val profile: ProfileViewUIItem

    data class Like(
        override val personId: PersonId,
        override val profile: ProfileViewUIItem
    ) : ReactionUIState

    data class SuperLike(
        override val personId: PersonId,
        override val profile: ProfileViewUIItem,
        val message: String
    ) : ReactionUIState
}

fun ReactionsToMe.toUIState(): ReactionUIState {
    return when (this) {
        is ReactionsToMe.Like -> ReactionUIState.Like(personId, profile.toPersonView())
        is ReactionsToMe.SuperLike -> ReactionUIState.SuperLike(
            personId,
            profile.toPersonView(),
            message
        )
    }
}
