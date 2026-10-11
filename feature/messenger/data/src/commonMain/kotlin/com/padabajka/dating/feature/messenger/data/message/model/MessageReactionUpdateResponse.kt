package com.padabajka.dating.feature.messenger.data.message.model

import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import kotlinx.serialization.Serializable

@Serializable
class MessageReactionUpdateResponse(
    val author: String,
    val value: MessageReaction.Value,
    val time: Long,
    val message: MessageDto.Existing,
)
