package com.padabajka.dating.core.repository.api.model.swiper

sealed interface Reaction

sealed class PersonReaction(open val id: PersonId) : Reaction {
    data class SuperLike(
        override val id: PersonId,
        val message: String
    ) : PersonReaction(id)
    data class Like(override val id: PersonId) : PersonReaction(id)
    data class Dislike(override val id: PersonId) : PersonReaction(id)
}
