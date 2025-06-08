package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.RawMessage
import com.padabajka.dating.feature.push.data.domain.model.DataPush

class HandleEditedMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(dataPush: DataPush.EditedMessage) {
        val chatId = dataPush.chatId.run(::ChatId)
        messageRepository.updateLocalMessage(chatId, dataPush.id) { message ->
            message.merge(dataPush)
        }
    }

    private fun RawMessage.merge(dataPush: DataPush.EditedMessage): RawMessage {
        return copy(
            editedAt = dataPush.editedAt,
            content = dataPush.content,
            parentMessageId = dataPush.parentMessageId
        )
    }
}
