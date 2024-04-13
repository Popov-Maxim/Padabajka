package com.fp.padabajka.feature.swiper.data.reaction.source

import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction
import com.fp.padabajka.feature.swiper.data.reaction.network.ReactionApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RemoteReactionDataSourceImpl(
    private val reactionApi: ReactionApi
) : RemoteReactionDataSource {

    private val mutex = Mutex()
    private val reactions: MutableSet<PersonReaction> = mutableSetOf()

    override suspend fun sendReaction(personReaction: PersonReaction) {
        addReaction(personReaction)
    }

    private suspend fun addReaction(reaction: PersonReaction) {
        mutex.withLock {
            reactions.add(reaction)

            if (reactionsPostRequired()) {
                reactionApi.postReactions(reactions)
                reactions.clear()
            }
        }
    }

    private fun reactionsPostRequired(): Boolean {
        return reactions.size > REACTIONS_CAPACITY ||
            reactions.any { it !is PersonReaction.Dislike }
    }

    companion object {
        private const val REACTIONS_CAPACITY = 10
    }
}
