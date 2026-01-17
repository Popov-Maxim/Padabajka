package com.padabajka.dating.core.data.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class ReactionType(val raw: String) {
    SuperLike("superlike"),
    Like("like"),
    Dislike("dislike")
}
