package com.fp.padabajka.core.repository.api.model.swiper

sealed class Reaction(val id: PersonId) {
    class SupperLike(id: PersonId) : Reaction(id)
    class Like(id: PersonId) : Reaction(id)
    class Dislike(id: PersonId) : Reaction(id)
}
