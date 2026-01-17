package com.padabajka.dating.feature.reaction.screen.domain

import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReactionsToMeUseCase(
    private val reactionRepository: ReactionRepository,
    private val profileRepository: ProfileRepository
) {
    val reactionsToMe: Flow<List<ReactionsToMe>> = reactionRepository.reactionsToMe.map { reactions ->
        val reactionsToMe = reactions.map { reaction ->
            profileRepository.profile(reaction.id).let { profile ->
                reaction.toReactionsToMe(profile)
            }
        }
        reactionsToMe
    }
}
