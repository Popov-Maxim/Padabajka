package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.DataPush
import com.padabajka.dating.core.data.network.incoming.dto.toDomain
import com.padabajka.dating.core.repository.api.UserPresenceRepository

class HandleUsersPresenceUseCase(
    private val userPresenceRepository: UserPresenceRepository
) {
    suspend operator fun invoke(dataPush: DataPush.UsersPresence) {
        val userPresences = dataPush.list.map { it.toDomain() }
        userPresenceRepository.setUserPresences(userPresences)
    }

    suspend operator fun invoke(dataPush: DataPush.UserPresence) {
        val userPresence = dataPush.userPresence.toDomain()
        userPresenceRepository.updateUserPresences(userPresence)
    }
}
