package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.Person

fun Match.toEntry(): MatchEntry {
    return MatchEntry(
        id = id.raw,
        personId = person.id.raw,
        chatId = chatId.raw,
        creationTime = creationTime
    )
}

fun MatchEntry.toMatch(person: Person): Match {
    return Match(
        id = Match.Id(id),
        person = person,
        chatId = chatId.let(::ChatId),
        creationTime = creationTime
    )
}
