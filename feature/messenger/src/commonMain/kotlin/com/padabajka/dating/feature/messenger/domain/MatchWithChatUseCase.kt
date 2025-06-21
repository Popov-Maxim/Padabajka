package com.padabajka.dating.feature.messenger.domain

import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.MessageRepository
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
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<List<MatchWithChat>> {
        return matchRepository.matches().flatMapLatest { matches ->
            val chatFlows = matches
                .map { match ->
                    val chatId = match.chatId
                    messageRepository.lastMessage(chatId).map {
                        val unreadMessagesCount = messageRepository.unreadMessagesCount(chatId)
                        Chat(chatId, it, unreadMessagesCount)
                    }.map {
                        match.toMatchWithChat(it)
                    }
                }

            if (chatFlows.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(chatFlows) { it.toList() }
            }
        }
    }
}
