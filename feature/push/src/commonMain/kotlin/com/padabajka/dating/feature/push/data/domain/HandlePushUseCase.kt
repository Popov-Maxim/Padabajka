package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.DataPush
import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.feature.push.data.domain.model.MessagePush

class HandlePushUseCase(
    private val dataPushParser: DataPushParser,
    private val handleNewMatchUseCase: HandleNewMatchUseCase,
    private val handleNewMessageUseCase: HandleNewMessageUseCase,
    private val handleDeleteMessageUseCase: HandleDeleteMessageUseCase,
    private val handleEditedMessageUseCase: HandleEditedMessageUseCase,
    private val handleUsersPresenceUseCase: HandleUsersPresenceUseCase,
    private val handleNewReactionToMeUseCase: HandleNewReactionToMeUseCase,
    private val handleReadMessageEventUseCase: HandleReadMessageEventUseCase
) {
    suspend operator fun invoke(rawPush: MessagePush) {
        val dataPush = dataPushParser.parse(rawPush) ?: return
        when (dataPush) {
            is MessageDataPush.NewMatch -> handleNewMatchUseCase(dataPush)
            is MessageDataPush.NewMessage -> handleNewMessageUseCase(dataPush)
            is MessageDataPush.DeleteMessage -> handleDeleteMessageUseCase(dataPush)
            is MessageDataPush.EditedMessage -> handleEditedMessageUseCase(dataPush)
            is MessageDataPush.ReadMessageEvent -> handleReadMessageEventUseCase(dataPush)
            is DataPush.UsersPresence -> handleUsersPresenceUseCase(dataPush)
            is DataPush.UserPresence -> handleUsersPresenceUseCase(dataPush)
            is DataPush.NewReactionToMe -> handleNewReactionToMeUseCase(dataPush)
        }
    }
}
