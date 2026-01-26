package com.padabajka.dating.feature.messenger.data.message.source.remote.routes

import com.padabajka.dating.core.repository.api.model.messenger.ChatId

object ChatRoutes {

    const val CHATS = "chats"

    fun chat(chatId: ChatId) =
        "$CHATS/$chatId"

    fun markAsRead(chatId: ChatId) =
        "$CHATS/$chatId/read"
}
