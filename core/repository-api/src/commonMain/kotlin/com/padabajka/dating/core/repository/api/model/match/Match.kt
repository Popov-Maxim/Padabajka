package com.padabajka.dating.core.repository.api.model.match

import com.padabajka.dating.core.repository.api.model.match.Match.Id
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

data class Match(
    val id: Id,
    val person: Person,
    val chatId: ChatId,
    val creationTime: Long
) {
    @JvmInline
    @Serializable
    value class Id(val raw: String)
}

data class RawMatch(
    val id: Id,
    val personId: PersonId,
    val chatId: ChatId,
    val creationTime: Long
)
