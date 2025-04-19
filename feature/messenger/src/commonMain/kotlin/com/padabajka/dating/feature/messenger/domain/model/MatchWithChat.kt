package com.padabajka.dating.feature.messenger.domain.model

import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.swiper.Person

data class MatchWithChat(
    val id: Match.Id,
    val person: Person,
    val chat: Chat,
    val creationTime: Long
)

fun Match.toMatchWithChat(chat: Chat): MatchWithChat {
    return MatchWithChat(
        id = id,
        person = person,
        chat = chat,
        creationTime = creationTime
    )
}
