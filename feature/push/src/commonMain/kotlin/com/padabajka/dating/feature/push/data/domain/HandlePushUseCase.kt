package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.DataPush
import com.padabajka.dating.core.data.network.incoming.dto.MatchDataPush
import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.feature.push.data.domain.model.MessagePush

class HandlePushUseCase(
    private val dataPushParser: DataPushParser,
    private val authRepository: AuthRepository,
    private val handleNewMatchUseCase: HandleNewMatchUseCase,
    private val handleNewMessageUseCase: HandleNewMessageUseCase,
    private val handleDeleteMessageUseCase: HandleDeleteMessageUseCase,
    private val handleEditedMessageUseCase: HandleEditedMessageUseCase,
    private val handleUsersPresenceUseCase: HandleUsersPresenceUseCase,
    private val handleNewReactionToMeUseCase: HandleNewReactionToMeUseCase,
    private val handleSubscriptionUseCase: HandleSubscriptionUseCase,
    private val handleReadMessageEventUseCase: HandleReadMessageEventUseCase,
    private val handleDeleteChatEventUseCase: HandleDeleteChatEventUseCase,
    private val handleDeleteMatchUseCase: HandleDeleteMatchUseCase,
    private val handleUpdateMatchUseCase: HandleUpdateMatchUseCase,
    private val handleNotificationPayloadUseCase: HandleNotificationPayloadUseCase,
) {
    @Suppress("CyclomaticComplexMethod", "ReturnCount")
    suspend operator fun invoke(rawPush: MessagePush) {
        println("HandlePushUseCase: rawPush = ${rawPush.dataJson}")

        val userId = authRepository.currentAuthState.userIdOrNull() ?: return
        val pushMessage = dataPushParser.parse(rawPush) ?: return
        if (pushMessage.recipientUserId != userId) return

        val dataPush = pushMessage.event
        println("HandlePushUseCase: dataPush = $dataPush")
        when (dataPush) {
            is DataPush.UsersPresence -> handleUsersPresenceUseCase(dataPush)
            is DataPush.UserPresence -> handleUsersPresenceUseCase(dataPush)
            is DataPush.NewReactionToMe -> handleNewReactionToMeUseCase(dataPush)
            is DataPush.FeatureUsage -> handleSubscriptionUseCase(dataPush)
            is DataPush.SubscriptionPlan -> handleSubscriptionUseCase(dataPush)

            is MessageDataPush.NewMessage -> handleNewMessageUseCase(dataPush)
            is MessageDataPush.DeleteMessage -> handleDeleteMessageUseCase(dataPush)
            is MessageDataPush.EditedMessage -> handleEditedMessageUseCase(dataPush)
            is MessageDataPush.ReadMessageEvent -> handleReadMessageEventUseCase(dataPush)
            is MessageDataPush.DeleteChatEvent -> handleDeleteChatEventUseCase(dataPush)

            is MatchDataPush.NewMatch -> handleNewMatchUseCase(dataPush)
            is MatchDataPush.DeleteMatch -> handleDeleteMatchUseCase(dataPush)
            is MatchDataPush.UpdateMatch -> handleUpdateMatchUseCase(dataPush)
        }

        pushMessage.notification?.let { notification ->
            handleNotificationPayloadUseCase.invoke(notification)
        }
    }
}
