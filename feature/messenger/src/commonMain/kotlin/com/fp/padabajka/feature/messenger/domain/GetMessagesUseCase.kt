package com.fp.padabajka.feature.messenger.domain

import com.fp.padabajka.core.repository.api.MessageRepository
import com.fp.padabajka.core.repository.api.model.messenger.Message
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(private val messageRepository: MessageRepository) {
    operator fun invoke(personId: PersonId): Flow<List<Message>> {
        // TODO: Add error handling
        return messageRepository.messages(personId)
    }
}
