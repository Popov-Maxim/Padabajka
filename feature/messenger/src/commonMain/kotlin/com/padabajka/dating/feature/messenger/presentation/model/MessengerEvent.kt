package com.padabajka.dating.feature.messenger.presentation.model

import com.padabajka.dating.core.repository.api.model.messenger.ChatId

sealed interface MessengerEvent

data class OpenChatEvent(val chatId: ChatId, val personItem: PersonItem) : MessengerEvent
