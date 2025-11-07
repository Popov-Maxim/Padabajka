package com.padabajka.dating.core.repository.api.model.swiper

sealed interface Reaction

data object AdReaction : Reaction

sealed class PersonReaction(val id: PersonId) : Reaction {
    class SuperLike(
        id: PersonId,
        val message: String
    ) : PersonReaction(id)
    class Like(id: PersonId) : PersonReaction(id)
    class Dislike(id: PersonId) : PersonReaction(id)
}
