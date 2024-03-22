package com.fp.padabajka.core.repository.api.model.messenger

import com.fp.padabajka.core.repository.api.model.swiper.PersonId

// TODO: Rename
sealed interface MessengerAction {
    val author: PersonId
}

data class StartedTypingAction(override val author: PersonId) : MessengerAction
data class StoppedTypingAction(override val author: PersonId) : MessengerAction

data class NewMessageAction(val message: Message) : MessengerAction {
    override val author: PersonId
        get() = message.author
}

data class MessageReactionAction(
    override val author: PersonId,
    val messageId: MessageId,
    val reaction: MessageReaction?
) : MessengerAction

data class MessageGotReadAction(
    override val author: PersonId,
    val messageId: MessageId
) : MessengerAction
