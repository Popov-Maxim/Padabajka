package com.fp.padabajka.feature.swiper.data.reaction.source

import com.fp.padabajka.core.repository.api.model.swiper.PersonReaction

interface RemoteReactionDataSource {
    suspend fun sendReaction(personReaction: PersonReaction)
}
