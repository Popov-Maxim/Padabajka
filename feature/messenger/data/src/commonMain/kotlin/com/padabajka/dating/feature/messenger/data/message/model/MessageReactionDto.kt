package com.padabajka.dating.feature.messenger.data.message.model

import com.padabajka.dating.component.room.messenger.entry.MessageReactionEntity
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import kotlinx.serialization.Serializable

@Serializable
class MessageReactionDto(
    val author: String,
    val value: MessageReaction.Value,
    val time: Long,
)

fun MessageReactionDto.toEntity(): MessageReactionEntity {
    return MessageReactionEntity(
        author = author,
        value = value,
        time = time,
    )
}

fun MessageReaction.toDto(): MessageReactionDto {
    return MessageReactionDto(
        author = author.id.raw,
        value = value,
        time = time,
    )
}
