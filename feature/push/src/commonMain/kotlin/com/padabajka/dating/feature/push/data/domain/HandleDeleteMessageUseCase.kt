package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource

class HandleDeleteMessageUseCase(
    private val localMessageDataSource: LocalMessageDataSource,
) {
    suspend operator fun invoke(dataPush: MessageDataPush.DeleteMessage) {
        localMessageDataSource.applyRemoteDeletion(
            chatId = dataPush.chatId,
            messageId = dataPush.id.raw,
            version = dataPush.version,
            fromEvent = true,
        )
    }
}
