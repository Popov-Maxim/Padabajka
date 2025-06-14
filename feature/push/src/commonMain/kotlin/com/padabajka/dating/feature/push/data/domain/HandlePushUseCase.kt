package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.model.push.DataPush
import com.padabajka.dating.feature.push.data.domain.model.MessagePush

class HandlePushUseCase(
    private val dataPushParser: DataPushParser,
    private val handleNewMatchUseCase: HandleNewMatchUseCase,
    private val handleNewMessageUseCase: HandleNewMessageUseCase,
    private val handleDeleteMessageUseCase: HandleDeleteMessageUseCase,
    private val handleEditedMessageUseCase: HandleEditedMessageUseCase
) {
    suspend operator fun invoke(rawPush: MessagePush) {
        val dataPush = dataPushParser.parse(rawPush) ?: return
        when (dataPush) {
            is DataPush.NewMatch -> handleNewMatchUseCase(dataPush)
            is DataPush.NewMessage -> handleNewMessageUseCase(dataPush)
            is DataPush.DeleteMessage -> handleDeleteMessageUseCase(dataPush)
            is DataPush.EditedMessage -> handleEditedMessageUseCase(dataPush)
        }
    }
}
