package com.padabajka.dating.feature.profile.presentation.model

sealed interface ProfileViewMode {

    data class Discovery(
        val onLike: () -> Unit,
        val onDislike: () -> Unit,
    ) : ProfileViewMode

    data class Match(
        val onDeleteChat: () -> Unit,
        val onUnmatch: () -> Unit,
    ) : ProfileViewMode

    data object None : ProfileViewMode
}
