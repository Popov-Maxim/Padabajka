package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalChatDataSource

class HandleDeleteChatEventUseCase(
    private val localChatDataSource: LocalChatDataSource,
) {
    suspend operator fun invoke(dataPush: MessageDataPush.DeleteChatEvent) {
        localChatDataSource.deleteChat(dataPush.chatId)
    }
}
