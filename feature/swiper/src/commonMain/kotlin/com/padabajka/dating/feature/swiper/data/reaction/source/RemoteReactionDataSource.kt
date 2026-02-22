package com.padabajka.dating.feature.swiper.data.reaction.source

import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction

interface RemoteReactionDataSource {
    suspend fun sendReaction(personReaction: PersonReaction)
    suspend fun forceSendReactions()
    suspend fun reactionsToMe(): List<PersonReaction>
}
