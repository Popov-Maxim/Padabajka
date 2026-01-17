package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId

class HandleDeleteMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(dataPush: MessageDataPush.DeleteMessage) {
        val id = dataPush.id
        val chatId = dataPush.chatId.run(::ChatId)
        messageRepository.deleteLocalMessage(chatId, id)
    }
}
