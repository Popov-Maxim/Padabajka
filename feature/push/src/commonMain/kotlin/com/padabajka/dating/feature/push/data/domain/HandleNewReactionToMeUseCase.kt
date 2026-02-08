package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.DataPush
import com.padabajka.dating.core.data.network.model.ReactionType
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction

class HandleNewReactionToMeUseCase(
    private val reactionRepository: ReactionRepository
) {
    suspend operator fun invoke(dataPush: DataPush.NewReactionToMe) {
        val reaction = dataPush.toDomain()
        reactionRepository.addReactionsToMe(reaction)
    }

    private fun DataPush.NewReactionToMe.toDomain(): PersonReaction {
        val personId = fromUserId.raw.run(::PersonId)
        return when (reaction) {
            ReactionType.SuperLike -> PersonReaction.SuperLike(personId, message ?: "")
            ReactionType.Like -> PersonReaction.Like(personId)
            ReactionType.Dislike -> PersonReaction.Dislike(personId)
        }
    }
}
