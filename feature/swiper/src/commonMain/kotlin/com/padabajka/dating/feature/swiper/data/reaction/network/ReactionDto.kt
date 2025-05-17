package com.padabajka.dating.feature.swiper.data.reaction.network

import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import kotlinx.serialization.Serializable

@Serializable
data class ReactionDto(
    val reactedPersonId: PersonId,
    val reaction: ReactionType
)

@Serializable
enum class ReactionType(val raw: String) {
    SuperLike("superlike"),
    Like("like"),
    Dislike("dislike")
}

fun PersonReaction.toReactionDto(): ReactionDto {
    return ReactionDto(
        reactedPersonId = id,
        reaction = when (this) {
            is PersonReaction.SuperLike -> ReactionType.SuperLike
            is PersonReaction.Like -> ReactionType.Like
            is PersonReaction.Dislike -> ReactionType.Dislike
        }
    )
}
