package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.match.Match
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    suspend fun matches(): Flow<List<Match>>
    suspend fun saveMatch(match: Match)
}
