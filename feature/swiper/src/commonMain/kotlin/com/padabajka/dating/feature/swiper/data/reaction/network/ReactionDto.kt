package com.padabajka.dating.feature.swiper.data.reaction.network

import com.padabajka.dating.core.data.network.model.ReactionType
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import kotlinx.serialization.Serializable

object ReactionDto {
    @Serializable
    data class Request(
        val toPersonId: PersonId,
        val reaction: ReactionType,
        val message: String? = null
    )

    @Serializable
    data class ToMeResponse(
        val fromPersonId: PersonId,
        val reaction: ReactionType,
        val message: String? = null
    )
}

fun PersonReaction.toRequest(): ReactionDto.Request {
    return ReactionDto.Request(
        toPersonId = id,
        reaction = when (this) {
            is PersonReaction.SuperLike -> ReactionType.SuperLike
            is PersonReaction.Like -> ReactionType.Like
            is PersonReaction.Dislike -> ReactionType.Dislike
        },
        message = (this as? PersonReaction.SuperLike)?.message
    )
}

fun ReactionDto.ToMeResponse.toDomain(): PersonReaction {
    return when (reaction) {
        ReactionType.SuperLike -> PersonReaction.SuperLike(fromPersonId, message ?: "")
        ReactionType.Like -> PersonReaction.Like(fromPersonId)
        ReactionType.Dislike -> TODO("bad reaction format")
    }
}
