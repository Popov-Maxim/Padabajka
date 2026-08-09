package com.padabajka.dating.feature.swiper.data.reaction.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.swiper.data.reaction.network.ReactionDto
import com.padabajka.dating.feature.swiper.data.reaction.network.toRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class LocalReactionDataSource(
    private val reactions: DataStore<List<ReactionDto.Request>>,
) {

    private val _reactionsToMe: MutableStateFlow<List<PersonReaction>> = MutableStateFlow(listOf())
    val reactionsToMe: Flow<List<PersonReaction>> = _reactionsToMe.asStateFlow()

    fun setReactionsToMe(reactions: List<PersonReaction>) {
        _reactionsToMe.value = reactions.distinctBy { it.id }
    }

    fun addReactionsToMe(reactions: List<PersonReaction>) {
        val newReactionsByPersonId = reactions.associateBy { it.id }
        _reactionsToMe.update { currentReactions ->
            currentReactions.filterNot { it.id in newReactionsByPersonId } +
                newReactionsByPersonId.values
        }
    }

    fun removeReactionToMe(personId: PersonId) {
        _reactionsToMe.update { reactions ->
            reactions.filterNot { it.id == personId }
        }
    }

    fun clearToMe() {
        _reactionsToMe.update {
            emptyList()
        }
    }

    suspend fun insert(reaction: PersonReaction): List<ReactionDto.Request> {
        return reactions.updateData {
            it + reaction.toRequest()
        }
    }

    suspend fun getReactions(): List<ReactionDto.Request> {
        return reactions.data.first()
    }

    suspend fun remove(reactions: List<ReactionDto.Request>) {
        this.reactions.updateData {
            it - reactions
        }
    }

    suspend fun clearAll() {
        reactions.updateData { emptyList() }
        clearToMe()
    }
}
