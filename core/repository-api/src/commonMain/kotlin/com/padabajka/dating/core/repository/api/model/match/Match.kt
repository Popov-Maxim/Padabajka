package com.padabajka.dating.core.repository.api.model.match

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.Person
import kotlin.jvm.JvmInline

data class Match(
    val id: Id,
    val person: Person,
    val chatId: ChatId,
    val creationTime: Long
) {
    @JvmInline
    value class Id(val raw: String)
}
