package com.padabajka.dating.feature.swiper.data.reaction.source

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalReactionDataSource {

    private val _reactionsToMe: MutableStateFlow<List<PersonReaction>> = MutableStateFlow(listOf())
    val reactionsToMe: Flow<List<PersonReaction>> = _reactionsToMe.asStateFlow()

    fun addReactionsToMe(reactions: List<PersonReaction>) {
        _reactionsToMe.update {
            it + reactions
        }
    }
}
