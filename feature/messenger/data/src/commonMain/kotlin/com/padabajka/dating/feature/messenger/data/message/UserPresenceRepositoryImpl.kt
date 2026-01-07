package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.core.repository.api.UserPresenceRepository
import com.padabajka.dating.core.repository.api.model.profile.UserPresence
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class UserPresenceRepositoryImpl : UserPresenceRepository {
    private val _presenceMap = MutableStateFlow<Map<PersonId, UserPresence>>(emptyMap())
    override fun userPresenceFlow(personId: PersonId): Flow<UserPresence> {
        return _presenceMap
            .map { it[personId] }
            .distinctUntilChanged()
            .filterNotNull()
    }

    override fun currentUserPresence(personId: PersonId): UserPresence? {
        val value = _presenceMap.value
        return value[personId]
    }

    override suspend fun setUserPresences(userPresence: List<UserPresence>) {
        val newMap = userPresence.associateBy(UserPresence::userId)
        _presenceMap.emit(newMap)
    }
}
