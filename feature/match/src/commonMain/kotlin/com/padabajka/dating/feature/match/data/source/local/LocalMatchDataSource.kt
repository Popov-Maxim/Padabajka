package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.flow.Flow

interface LocalMatchDataSource {
    fun matches(): Flow<List<MatchEntry>>
    fun findMatch(chatId: ChatId): Flow<MatchEntry>
    suspend fun saveMatch(match: MatchEntry)
    suspend fun replaceMatches(matches: List<MatchEntry>)
    suspend fun delete(matchId: Match.Id)

    suspend fun update(matchId: Match.Id, updated: (MatchEntry) -> MatchEntry)
}
