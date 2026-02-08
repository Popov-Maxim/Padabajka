package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.DataPush
import com.padabajka.dating.core.data.network.incoming.dto.MatchDataPush
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
    private val handleReadMessageEventUseCase: HandleReadMessageEventUseCase,
    private val handleDeleteChatEventUseCase: HandleDeleteChatEventUseCase,
    private val handleDeleteMatchUseCase: HandleDeleteMatchUseCase,
    private val handleUpdateMatchUseCase: HandleUpdateMatchUseCase,
) {
    suspend operator fun invoke(rawPush: MessagePush) {
        println("HandlePushUseCase: rawPush = ${rawPush.dataJson}")
        val dataPush = dataPushParser.parse(rawPush) ?: return
        println("HandlePushUseCase: dataPush = $dataPush")
        when (dataPush) {
            is DataPush.UsersPresence -> handleUsersPresenceUseCase(dataPush)
            is DataPush.UserPresence -> handleUsersPresenceUseCase(dataPush)
            is DataPush.NewReactionToMe -> handleNewReactionToMeUseCase(dataPush)

            is MessageDataPush.NewMessage -> handleNewMessageUseCase(dataPush)
            is MessageDataPush.DeleteMessage -> handleDeleteMessageUseCase(dataPush)
            is MessageDataPush.EditedMessage -> handleEditedMessageUseCase(dataPush)
            is MessageDataPush.ReadMessageEvent -> handleReadMessageEventUseCase(dataPush)
            is MessageDataPush.DeleteChatEvent -> handleDeleteChatEventUseCase(dataPush)

            is MatchDataPush.NewMatch -> handleNewMatchUseCase(dataPush)
            is MatchDataPush.DeleteMatch -> handleDeleteMatchUseCase(dataPush)
            is MatchDataPush.UpdateMatch -> handleUpdateMatchUseCase(dataPush)
        }
    }
}
