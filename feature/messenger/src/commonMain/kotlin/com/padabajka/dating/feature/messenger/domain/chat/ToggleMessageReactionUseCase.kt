package com.padabajka.dating.feature.messenger.domain.chat

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.MessageRepository
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction

class ToggleMessageReactionUseCase(
    private val messageRepository: MessageRepository,
    private val authRepository: AuthRepository,
    private val reactToMessageUseCase: ReactToMessageUseCase
) {
    suspend operator fun invoke(chatId: ChatId, messageId: MessageId, reactionValue: MessageReaction.Value) {
        val message = messageRepository.message(chatId, messageId) ?: return
        val currentUserId = authRepository.currentAuthState.userIdOrNull() ?: return
        val userHasReacted = message.reactions.any {
            it.author.id.raw == currentUserId.raw && it.value == reactionValue
        }

        if (userHasReacted) {
            reactToMessageUseCase.invoke(messageId, null)
        } else {
            reactToMessageUseCase.invoke(messageId, reactionValue)
        }
    }
}
