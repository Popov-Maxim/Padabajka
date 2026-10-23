package com.padabajka.dating.feature.messenger.domain

import com.padabajka.dating.core.domain.retryLoading
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.domain.model.Chat
import com.padabajka.dating.feature.messenger.domain.model.MatchWithChat
import com.padabajka.dating.feature.messenger.domain.model.toMatchWithChat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MatchWithChatUseCase(
    private val matchRepository: MatchRepository,
    private val messageRepository: MessageRepository,
    private val personRepository: PersonRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<MatchWithChat>> {
        return matchRepository.rawMatches().flatMapLatest { matches ->
            val chatFlows = matches
                .map { match ->
                    val chatId = match.chatId
                    val chatFlow = observeChat(chatId)
                    combine(personRepository.observePerson(match.personId), chatFlow) { person, chat ->
                        match.toMatchWithChat(chat, person)
                    }
                }

            if (chatFlows.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(chatFlows) { it.toList() }
            }
        }
    }

    private fun observeChat(chatId: ChatId): Flow<Chat> =
        messageRepository.lastMessage(chatId)
            .map { message ->
                Chat(chatId, message, messageRepository.unreadMessagesCount(chatId))
            }
            .retryLoading(fallback = Chat(chatId, lastMessage = null, unreadMessagesCount = 0))
}
