package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.RawMessage

class HandleEditedMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(dataPush: MessageDataPush.EditedMessage) {
        val chatId = dataPush.chatId.run(::ChatId)
        messageRepository.updateLocalMessage(chatId, dataPush.id) { message ->
            message.merge(dataPush)
        }
    }

    private fun RawMessage.merge(dataPush: MessageDataPush.EditedMessage): RawMessage {
        return copy(
            editedAt = dataPush.editedAt,
            content = dataPush.content,
            parentMessageId = dataPush.parentMessageId
        )
    }
}
