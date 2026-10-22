package com.padabajka.dating.feature.reaction.screen.domain

import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReactionsToMeUseCase(
    reactionRepository: ReactionRepository,
    private val personRepository: PersonRepository
) {
    val reactionsToMe: Flow<List<ReactionsToMe>> = reactionRepository.reactionsToMe.map { reactions ->
        val reactionsToMe = reactions.mapNotNull { reaction ->
            runCatching {
                personRepository.getPerson(reaction.id)?.let { person ->
                    reaction.toReactionsToMe(person.profile)
                }
            }.getOrNull() // TODO(P1): handle error
        }
        reactionsToMe
    }
}
