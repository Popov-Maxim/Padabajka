package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.match.RawMatch
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    fun matches(): Flow<List<Match>>
    suspend fun findMatch(chatId: ChatId): Flow<Match>
    suspend fun saveMatch(match: RawMatch)
    suspend fun sync()

    suspend fun deleteMatch(match: Match.Id)
}
