package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.feature.messenger.data.message.source.local.applyRemoteMessage
import com.padabajka.dating.feature.messenger.data.message.source.local.LocalMessageDataSource

class HandleNewMessageUseCase(
    private val localMessageDataSource: LocalMessageDataSource
) {
    suspend operator fun invoke(dataPush: MessageDataPush.NewMessage) {
        localMessageDataSource.applyRemoteMessage(dataPush)
    }
}
