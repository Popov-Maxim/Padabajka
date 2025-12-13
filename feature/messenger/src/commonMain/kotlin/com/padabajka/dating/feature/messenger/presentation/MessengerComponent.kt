package com.padabajka.dating.feature.messenger.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.domain.MatchWithChatUseCase
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.toMessageItem
import com.padabajka.dating.feature.messenger.presentation.model.ChatItem
import com.padabajka.dating.feature.messenger.presentation.model.EmptyChatItem
import com.padabajka.dating.feature.messenger.presentation.model.MatchItem
import com.padabajka.dating.feature.messenger.presentation.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.model.MessengerState
import com.padabajka.dating.feature.messenger.presentation.model.OpenChatEvent
import com.padabajka.dating.feature.messenger.presentation.model.toPersonItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.map

class MessengerComponent(
    context: ComponentContext,
    private val openChat: (chatId: ChatId, matchItem: MatchItem) -> Unit,
    private val matchWithChatUseCase: MatchWithChatUseCase,
) : BaseComponent<MessengerState>(
    context,
    MessengerState(persistentListOf(), persistentListOf())
) {

    init {
        mapAndReduceException(
            action = {
                matchWithChatUseCase().map {
                    it.map { match ->
                        val person = match.person.toPersonItem()
                        val matchItem = MatchItem(match.id, person, match.creationTime)
                        val lastMessage = match.chat.lastMessage
                        if (lastMessage == null) {
                            EmptyChatItem(match.chat.id, matchItem)
                        } else {
                            ChatItem(
                                chatId = match.chat.id,
                                match = matchItem,
                                lastMessage = lastMessage.toMessageItem(),
                                unreadMessagesCount = match.chat.unreadMessagesCount
                            )
                        }
                    }
                }.map { matches ->
                    val matchesWithoutChat = matches.filterIsInstance<EmptyChatItem>()
                        .sortedByDescending { chat -> chat.match.creationTime }
                    val chats = matches.filterIsInstance<ChatItem>()
                        .sortedByDescending { chat -> chat.lastMessage.sentTime }
                    matchesWithoutChat to chats
                }.collect { (matchesWithoutChat, chats) ->
                    reduce { state ->
                        state.copy(
                            matchesWithoutChat = matchesWithoutChat.toPersistentList(),
                            chats = chats.toPersistentList()
                        )
                    }
                }
            },
            mapper = { TODO(it.toString()) },
            update = { state, _ -> state }
        )
    }

    fun onEvent(event: MessengerEvent) {
        when (event) {
            is OpenChatEvent -> openChat(event.chatId, event.matchItem)
        }
    }
}
