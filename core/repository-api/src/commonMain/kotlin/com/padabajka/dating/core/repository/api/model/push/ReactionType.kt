package com.padabajka.dating.core.repository.api.model.push

import kotlinx.serialization.Serializable

@Serializable
enum class ReactionType(val raw: String) {
    SuperLike("superlike"),
    Like("like"),
    Dislike("dislike")
}
