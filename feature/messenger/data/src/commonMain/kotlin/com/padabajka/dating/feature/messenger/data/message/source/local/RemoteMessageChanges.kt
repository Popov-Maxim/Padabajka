package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.entry.MessageContentDB
import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.toEntity

suspend fun LocalMessageDataSource.applyRemoteMessage(message: MessageDto) = when (message) {
    is MessageDto.Existing -> applyRemoteMessage(message.chatId, message.id, message.version) {
        message.toEntity()
    }
    is MessageDto.Edited -> applyRemoteMessage(message.chatId, message.id, message.version) { current ->
        current?.let { message.message.toEntity() }
    }
    is MessageDto.Deleted -> applyRemoteDeletion(message.chatId, message.id, message.version)
}

suspend fun LocalMessageDataSource.applyRemoteMessage(
    message: MessageDataPush.NewMessage,
) = applyRemoteMessage(message.chatId, message.id.raw, message.version, fromEvent = true) {
    message.toEntity()
}

suspend fun LocalMessageDataSource.applyRemoteMessage(
    message: MessageDataPush.EditedMessage,
) = applyRemoteMessage(message.chatId, message.id.raw, message.version, fromEvent = true) { current ->
    current?.copy(
        editedAt = message.editedAt,
        editSynced = true,
        content = MessageContentDB(message.content),
        parentMessageId = message.parentMessageId?.raw,
        reactions = message.reactions.map { it.toEntity() },
    )
}
