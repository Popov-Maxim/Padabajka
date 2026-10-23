package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.match.RawMatch
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

fun Match.toEntry(): MatchEntry {
    return MatchEntry(
        id = id.raw,
        personId = personId.raw,
        chatId = chatId.raw,
        creationTime = creationTime
    )
}

fun RawMatch.toEntry(): MatchEntry {
    return MatchEntry(
        id = id.raw,
        personId = personId.raw,
        chatId = chatId.raw,
        creationTime = creationTime
    )
}

fun MatchEntry.toMatch(person: Person?): Match {
    return Match(
        id = Match.Id(id),
        personId = PersonId(personId),
        person = person,
        chatId = chatId.let(::ChatId),
        creationTime = creationTime
    )
}

fun MatchEntry.toRawMatch(): RawMatch {
    val match = this
    return RawMatch(
        id = Match.Id(match.id),
        personId = PersonId(match.personId),
        chatId = ChatId(match.chatId),
        creationTime = match.creationTime,
    )
}
