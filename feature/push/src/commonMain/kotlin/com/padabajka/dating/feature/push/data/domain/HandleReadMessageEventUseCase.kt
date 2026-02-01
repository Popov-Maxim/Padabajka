package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.component.room.messenger.entry.toEntity
import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatReadStateDataSource

class HandleReadMessageEventUseCase(
    private val localReadMessageDataSource: LocalChatReadStateDataSource,
) {
    suspend operator fun invoke(dataPush: MessageDataPush.ReadMessageEvent) {
        val messageReadEvent = dataPush.toEntity()
        localReadMessageDataSource.addMessageReadEvent(messageReadEvent)
    }
}
