package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.messenger.Message
import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun messages(matchId: PersonId): Flow<List<Message>>
    suspend fun sendMessage(matchId: PersonId, content: String, parentMessageId: MessageId? = null)
    suspend fun readMessage(matchId: PersonId, messageId: MessageId)
    suspend fun reactToMessage(matchId: PersonId, messageId: MessageId, reaction: MessageReaction)
}
