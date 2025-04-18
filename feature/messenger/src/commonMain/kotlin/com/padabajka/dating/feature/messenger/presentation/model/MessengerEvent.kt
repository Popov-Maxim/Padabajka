package com.padabajka.dating.feature.messenger.presentation.model

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

sealed interface MessengerEvent

data class OpenChatEvent(val chatId: ChatId) : MessengerEvent
data class NewChatEvent(val personId: PersonId) : MessengerEvent
