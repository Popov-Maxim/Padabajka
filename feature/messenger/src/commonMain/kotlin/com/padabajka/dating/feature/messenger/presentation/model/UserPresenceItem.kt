package com.padabajka.dating.feature.messenger.presentation.model

import androidx.compose.runtime.Composable
import com.padabajka.dating.core.repository.api.model.profile.UserPresence
import kotlinx.serialization.Serializable

@Serializable
sealed interface UserPresenceItem {
    @Serializable
    data object None : UserPresenceItem

    @Serializable
    data class Has(
        val online: Boolean,
        val description: String
    ) : UserPresenceItem
}

@Composable
fun UserPresenceItem.text(): String {
    return when (this) {
        is UserPresenceItem.Has -> description
        UserPresenceItem.None -> "wait..." // TODO: add translate
    }
}

fun UserPresence.toUI(): UserPresenceItem.Has {
    return UserPresenceItem.Has(
        online = online,
        description = description
    )
}
