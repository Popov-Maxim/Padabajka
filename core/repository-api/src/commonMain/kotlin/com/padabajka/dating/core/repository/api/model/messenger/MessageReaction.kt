package com.padabajka.dating.core.repository.api.model.messenger

import com.padabajka.dating.core.repository.api.model.swiper.Person
import kotlinx.serialization.Serializable

data class MessageReaction(
    val author: Person,
    val value: Value,
    val time: Long,
    val reactionSynced: Boolean = false,
) {
    @Serializable
    enum class Value {
        Like
    }
}
