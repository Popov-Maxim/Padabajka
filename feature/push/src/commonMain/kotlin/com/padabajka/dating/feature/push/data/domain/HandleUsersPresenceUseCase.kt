package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.UserPresenceRepository
import com.padabajka.dating.core.repository.api.model.push.DataPush
import com.padabajka.dating.core.repository.api.model.push.toDomain

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
