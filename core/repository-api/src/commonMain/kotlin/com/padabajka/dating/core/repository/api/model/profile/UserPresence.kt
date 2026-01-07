package com.padabajka.dating.core.repository.api.model.profile

import com.padabajka.dating.core.repository.api.model.swiper.PersonId

class UserPresence(
    val userId: PersonId,
    val online: Boolean,
    val description: String
)
