package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.profile.UserPresence
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.coroutines.flow.Flow

interface UserPresenceRepository {
    fun userPresenceFlow(personId: PersonId): Flow<UserPresence>
    fun currentUserPresence(personId: PersonId): UserPresence?

    suspend fun setUserPresences(userPresence: List<UserPresence>)
}
