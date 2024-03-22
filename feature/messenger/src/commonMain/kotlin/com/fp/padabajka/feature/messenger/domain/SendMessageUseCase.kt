package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.model.messenger.MessageId
import com.fp.padabajka.core.repository.api.model.swiper.PersonId

class SendMessageUseCase {
    suspend operator fun invoke(
        addressee: PersonId,
        messageText: String,
        parentMessageId: MessageId?
    ) {
        TODO("implement me")
    }
}
