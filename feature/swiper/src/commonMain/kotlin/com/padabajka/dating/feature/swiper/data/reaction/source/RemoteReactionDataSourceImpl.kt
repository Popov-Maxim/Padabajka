package com.padabajka.dating.feature.swiper.data.reaction.source

import com.padabajka.dating.core.data.Atomic
import com.padabajka.dating.core.data.atomic
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionApi
import com.padabajka.dating.feature.swiper.data.reaction.network.toDomain
import com.padabajka.dating.feature.swiper.data.reaction.network.toRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RemoteReactionDataSourceImpl(
    private val scope: CoroutineScope,
    private val reactionApi: ReactionApi
) : RemoteReactionDataSource {

    private val reactions: Atomic<MutableSet<PersonReaction>> = atomic(mutableSetOf())

    private val postJob = SupervisorJob()

    override suspend fun sendReaction(personReaction: PersonReaction) {
        reactions {
            add(personReaction)
        }

        if (reactionsPostRequired()) {
            postReactions()
        }
    }

    override suspend fun forceSendReactions() {
        postReactions().join()
    }

    override suspend fun reactionsToMe(): List<PersonReaction> {
        return reactionApi.getReactions().map { it.toDomain() }
    }

    private suspend fun reactionsPostRequired(): Boolean {
        return reactions {
            size > REACTIONS_CAPACITY || any { it !is PersonReaction.Dislike }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun postReactions(): Job {
        return scope.launch(postJob) {
            val reactionsToPost = reactions {
                toSet().also {
                    clear()
                }
            }

            if (reactionsToPost.isEmpty()) {
                return@launch
            }

            try {
                val reactions = reactionsToPost.map { it.toRequest() }.toSet()
                reactionApi.postReactions(reactions)
            } catch (e: Throwable) {
                e.printStackTrace()
                reactions {
                    addAll(reactionsToPost)
                }
            }
        }
    }

    companion object {
        private const val REACTIONS_CAPACITY = 10
    }
}
