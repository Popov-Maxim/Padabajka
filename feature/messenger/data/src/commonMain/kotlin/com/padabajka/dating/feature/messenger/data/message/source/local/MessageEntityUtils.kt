package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageReactionEntity
import com.padabajka.dating.core.data.network.incoming.dto.MessageReactionResponse
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionRequest

fun MessageEntry.addReaction(reaction: MessageReactionEntity): MessageEntry {
    return this.copy(reactions = this.reactions?.plus(reaction) ?: listOf(reaction))
}

fun MessageEntry.removeReaction(criterionForDelete: (MessageReactionEntity) -> Boolean): MessageEntry {
    return this.copy(reactions = this.reactions?.filter { criterionForDelete(it).not() })
}

fun MessageReaction.toEntity(): MessageReactionEntity {
    return MessageReactionEntity(
        author = author.id.raw,
        value = value,
        time = time,
        reactionSynced = reactionSynced
    )
}

fun MessageReactionResponse.toEntity(): MessageReactionEntity {
    return MessageReactionEntity(
        author = author,
        value = value,
        time = time,
        reactionSynced = true
    )
}

fun MessageReactionEntity.toSendRequestDto(): MessageReactionRequest.Send {
    return MessageReactionRequest.Send(
        reaction = value
    )
}

fun MessageReactionEntity.toRemoveRequestDto(): MessageReactionRequest.Remove {
    return MessageReactionRequest.Remove(
        reaction = value
    )
}
