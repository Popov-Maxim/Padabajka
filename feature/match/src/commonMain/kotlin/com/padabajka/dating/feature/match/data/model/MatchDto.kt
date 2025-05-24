package com.padabajka.dating.feature.match.data.model

import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.serialization.Serializable

@Serializable
class MatchDto(
    val id: Match.Id,
    val personId: PersonId,
    val chatId: ChatId,
    val creationTime: Long
)

fun MatchDto.toEntry(): MatchEntry {
    return MatchEntry(
        id = id.raw,
        personId = personId.raw,
        chatId = chatId.raw,
        creationTime = creationTime
    )
}
