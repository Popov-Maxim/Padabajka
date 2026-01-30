package com.padabajka.dating.core.data.network.incoming.dto

import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import kotlinx.serialization.Serializable

@Serializable
class MessageReactionResponse(
    val author: String,
    val value: MessageReaction.Value,
    val time: Long,
)
