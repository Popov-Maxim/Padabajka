package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.matches.entry.MatchEntry
import kotlinx.coroutines.flow.Flow

interface LocalMatchDataSource {
    suspend fun matches(): Flow<List<MatchEntry>>
    suspend fun saveMatch(match: MatchEntry)
}
