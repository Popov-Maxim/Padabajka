package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction

class ReactToMessageUseCase {
    suspend operator fun invoke(messageId: MessageId, reaction: MessageReaction) {
        TODO("Implement me")
    }
}
