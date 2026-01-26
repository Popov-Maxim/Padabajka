package com.padabajka.dating.feature.messenger.data.message.source.remote.routes

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

object MessageReactionRoutes {

    const val REACTIONS = "reactions"

    fun reactions(chatId: ChatId, messageId: MessageId) =
        "chats/$chatId/messages/$messageId/$REACTIONS"
}
