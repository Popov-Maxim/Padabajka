package com.padabajka.dating.core.data.network.incoming.dto

import com.padabajka.dating.core.repository.api.model.profile.UserPresence
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.serialization.Serializable

@Serializable
class UserPresenceDto(
    val userId: PersonId,
    val online: Boolean,
    val description: String
)

fun UserPresenceDto.toDomain(): UserPresence {
    return UserPresence(
        userId = userId,
        online = online,
        description = description
    )
}
