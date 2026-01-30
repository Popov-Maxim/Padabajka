package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource
import com.padabajka.dating.feature.messenger.data.message.source.local.toEntity

class HandleEditedMessageUseCase(
    private val localMessageDataSource: LocalMessageDataSource,
) {
    suspend operator fun invoke(dataPush: MessageDataPush.EditedMessage) {
        localMessageDataSource.updateMessage(dataPush.id.raw) { messageEntry ->
            messageEntry.merge(dataPush)
        }
    }

    private fun MessageEntry.merge(dataPush: MessageDataPush.EditedMessage): MessageEntry {
        return copy(
            editedAt = dataPush.editedAt,
            content = dataPush.content,
            parentMessageId = dataPush.parentMessageId?.raw,
            reactions = dataPush.reactions.map { it.toEntity() }
        )
    }
}
