package com.padabajka.dating.feature.messenger.domain.model

import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.match.RawMatch
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

data class MatchWithChat(
    val id: Match.Id,
    val personId: PersonId,
    val person: Person?,
    val chat: Chat,
    val creationTime: Long
)

fun RawMatch.toMatchWithChat(chat: Chat, person: Person?): MatchWithChat {
    return MatchWithChat(
        id = id,
        personId = personId,
        person = person,
        chat = chat,
        creationTime = creationTime
    )
}
