package com.fp.padabajka.core.repository.api.model.messenger

import com.fp.padabajka.core.repository.api.model.swiper.PersonId

data class ParentMessage(
    val id: MessageId,
    val author: PersonId,
    val content: String
)
