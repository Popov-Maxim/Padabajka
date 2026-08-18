package com.padabajka.dating.feature.swiper.data.reaction

import com.padabajka.dating.core.data.network.model.ReactionType
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.ReactionRepository
import com.padabajka.dating.core.repository.api.exception.SuperLikeException
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionDto
import com.padabajka.dating.feature.swiper.data.reaction.network.toRequest
import com.padabajka.dating.feature.swiper.data.reaction.source.LocalReactionDataSource
import com.padabajka.dating.feature.swiper.data.reaction.source.RemoteReactionDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ReactionRepositoryImpl(
    private val remoteReactionDataSource: RemoteReactionDataSource,
    private val localReactionDataSource: LocalReactionDataSource,
    private val matchRepository: MatchRepository
) : ReactionRepository {

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
            try {
                forceSendReactions(reactions)
            } catch (e: SuperLikeException) {
                val rejectedSuperLikes = reactions.filter { it.reaction == ReactionType.SuperLike }
                localReactionDataSource.remove(rejectedSuperLikes)
                throw e
            }
        }
    }

    override suspend fun forceReact(reaction: PersonReaction) {
        remoteReactionDataSource.sendReactions(listOf(reaction.toRequest()))
        localReactionDataSource.removeReactionToMe(reaction.id)
    }

    override suspend fun forceSendReactions() {
        val reactions = localReactionDataSource.getReactions()
        if (reactions.isNotEmpty()) {
            forceSendReactions(reactions)
        }
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

    override suspend fun clearLocalData() {
        localReactionDataSource.clearAll()
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
