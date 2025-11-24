package com.padabajka.dating.feature.reaction.screen.domain

import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.ReactionRepository

class ReactionsToMeUseCase(
    private val reactionRepository: ReactionRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): List<ReactionsToMe> {
        val reactions = reactionRepository.reactionsToMe()
        val reactionsToMe = reactions.map { reaction ->
            profileRepository.profile(reaction.id).let { profile ->
                reaction.toReactionsToMe(profile)
            }
        }
        return reactionsToMe
    }
}
