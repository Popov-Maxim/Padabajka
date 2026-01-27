package com.padabajka.dating.feature.messenger.data.message.source.remote.routes

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

object MessageRoutes {

    const val CHATS = "chats"
    const val MESSAGES = "messages"
    const val SYNC = "sync"

    fun messages(chatId: ChatId) =
        "$CHATS/${chatId.raw}/$MESSAGES"

    fun message(chatId: ChatId, messageId: MessageId) =
        "$CHATS/${chatId.raw}/$MESSAGES/${messageId.raw}"

    fun sync(chatId: ChatId) =
        "$CHATS/${chatId.raw}/$MESSAGES/$SYNC"
}
