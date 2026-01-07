package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.RawMessage
import com.padabajka.dating.core.repository.api.model.push.MessageDataPush
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

class HandleNewMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(dataPush: MessageDataPush.NewMessage) {
        val rawMessage = dataPush.toRawMessage()
        val chatId = dataPush.chatId.run(::ChatId)
        messageRepository.addLocalMessage(chatId, rawMessage)
    }

    private fun MessageDataPush.NewMessage.toRawMessage(): RawMessage {
        return RawMessage(
            id = id,
            authorId = authorId.run(::PersonId),
            content = content,
            creationTime = creationTime,
            parentMessageId = parentMessageId,
            editedAt = editedAt,
            readAt = readAt
        )
    }
}
