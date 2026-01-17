package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.push.DataPush
import com.padabajka.dating.core.repository.api.model.push.ReactionType
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction

class HandleNewReactionToMeUseCase(
    private val reactionRepository: ReactionRepository
) {
    suspend operator fun invoke(dataPush: DataPush.NewReactionToMe) {
        val reaction = dataPush.toDomain()
        reactionRepository.addReactionsToMe(reaction)
    }

    private fun DataPush.NewReactionToMe.toDomain(): PersonReaction {
        return when (reaction) {
            ReactionType.SuperLike -> PersonReaction.SuperLike(fromUserId, message ?: "")
            ReactionType.Like -> PersonReaction.Like(fromUserId)
            ReactionType.Dislike -> PersonReaction.Dislike(fromUserId)
        }
    }
}
