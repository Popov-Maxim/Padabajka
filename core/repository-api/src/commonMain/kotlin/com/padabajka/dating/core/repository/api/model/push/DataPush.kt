package com.padabajka.dating.core.repository.api.model.push

import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DataPush {
    @Serializable
    @SerialName("new_match")
    data class NewMatch(
        val id: Match.Id,
        val personId: PersonId,
        val chatId: ChatId,
        val creationTime: Long,

        val personName: String
    ) : DataPush

    @Serializable
    @SerialName("new_message")
    data class NewMessage(
        val id: MessageId,
        val chatId: String,
        val authorId: String,
        val content: String,
        val creationTime: Long,
        val parentMessageId: MessageId? = null,
        val editedAt: Long? = null,
        val readAt: Long? = null
    ) : DataPush

    @Serializable
    @SerialName("edited_message")
    data class EditedMessage(
        val id: MessageId,
        val chatId: String,
        val editedAt: Long,
        val content: String,
        val parentMessageId: MessageId? = null,
    ) : DataPush

    @Serializable
    @SerialName("delete_message")
    data class DeleteMessage(
        val id: MessageId,
        val chatId: String,
    ) : DataPush
}
