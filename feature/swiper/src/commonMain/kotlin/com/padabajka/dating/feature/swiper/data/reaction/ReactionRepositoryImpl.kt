package com.padabajka.dating.feature.swiper.data.reaction

import com.padabajka.dating.core.data.network.model.ReactionType
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionDto
import com.padabajka.dating.feature.swiper.data.reaction.source.LocalReactionDataSource
import com.padabajka.dating.feature.swiper.data.reaction.source.RemoteReactionDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ReactionRepositoryImpl(
    scope: CoroutineScope,
    private val remoteReactionDataSource: RemoteReactionDataSource,
    private val localReactionDataSource: LocalReactionDataSource,
    private val authRepository: AuthRepository,
    private val matchRepository: MatchRepository
) : ReactionRepository {

    init {
        scope.launch {
            authRepository.authState.collect {
                if (it.userIdOrNull() == null) {
                    localReactionDataSource.clearToMe()
                }
            }
        }
    }

    override val reactionsToMe: Flow<List<PersonReaction>>
        get() = combine(
            localReactionDataSource.reactionsToMe,
            matchRepository.matches()
        ) { reactions, matches ->
            val excludedPersonId = matches.map { it.person.id }.toSet()

            reactions.filter { it.id !in excludedPersonId }
        }

    override suspend fun react(reaction: PersonReaction) {
        val reactions = localReactionDataSource.insert(reaction)

        if (reactions.isNotEmpty() && reactions.requiredForSend()) {
            forceSendReactions(reactions)
        }
    }

    override suspend fun forceSendReactions() {
        val reactions = localReactionDataSource.getReactions()
        forceSendReactions(reactions)
    }

    override suspend fun deleteReaction() {
        remoteReactionDataSource.deleteReaction() // TODO(P1): add localReactionDataSource.remove
    }

    override suspend fun syncReactionsToMe() {
        val reactionsToMe = remoteReactionDataSource.reactionsToMe()
        localReactionDataSource.setReactionsToMe(reactionsToMe)
    }

    override suspend fun addReactionsToMe(reactions: PersonReaction) {
        localReactionDataSource.addReactionsToMe(listOf(reactions))
    }

    private suspend fun forceSendReactions(reactions: List<ReactionDto.Request>) {
        remoteReactionDataSource.sendReactions(reactions)
        localReactionDataSource.remove(reactions)
    }

    private fun List<ReactionDto.Request>.requiredForSend(): Boolean {
        return size > REACTIONS_CAPACITY || any { it.reaction != ReactionType.Dislike }
    }

    companion object {
        private const val REACTIONS_CAPACITY = 10
    }
}
