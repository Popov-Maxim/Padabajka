package com.padabajka.dating.feature.messenger.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.UserPresenceRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.domain.MatchWithChatUseCase
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.toMessageItem
import com.padabajka.dating.feature.messenger.presentation.model.ChatItem
import com.padabajka.dating.feature.messenger.presentation.model.EmptyChatItem
import com.padabajka.dating.feature.messenger.presentation.model.MatchItem
import com.padabajka.dating.feature.messenger.presentation.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.model.MessengerState
import com.padabajka.dating.feature.messenger.presentation.model.OpenChatEvent
import com.padabajka.dating.feature.messenger.presentation.model.UserPresenceItem
import com.padabajka.dating.feature.messenger.presentation.model.toPersonItem
import com.padabajka.dating.feature.messenger.presentation.model.toUI
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class MessengerComponent(
    context: ComponentContext,
    private val openChat: (chatId: ChatId, matchItem: MatchItem) -> Unit,
    private val matchWithChatUseCase: MatchWithChatUseCase,
    private val userPresenceRepository: UserPresenceRepository
) : BaseComponent<MessengerState>(
    context,
    MessengerState(persistentListOf(), persistentListOf())
) {

    init {
        init()
    }

    fun onEvent(event: MessengerEvent) {
        when (event) {
            is OpenChatEvent -> openChat(event.chatId, event.matchItem)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun init() {
        mapAndReduceException(
            action = {
                matchWithChatUseCase().flatMapLatest {
                    val matchFlows = it.map { match ->
                        val person = match.person.toPersonItem()
                        val personId = match.person.id
                        val currentUserPresence =
                            userPresenceRepository.currentUserPresence(personId)?.toUI()
                                ?: UserPresenceItem.None
                        userPresenceRepository.userPresenceFlow(personId)
                            .map {
                                val ui: UserPresenceItem = it.toUI()
                                ui
                            }
                            .onStart { emit(currentUserPresence) }
                            .distinctUntilChanged()
                            .map {
                                val currentUserPresence =
                                    userPresenceRepository.currentUserPresence(personId)?.toUI()
                                        ?: UserPresenceItem.None
                                val matchItem =
                                    MatchItem(match.id, person, match.creationTime, currentUserPresence)
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
                    }
                    combine(matchFlows) { items -> items.toList() }
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
}
