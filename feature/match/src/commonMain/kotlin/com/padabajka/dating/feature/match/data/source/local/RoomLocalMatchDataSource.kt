package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.matches.MatchesDao
import com.padabajka.dating.component.room.matches.entry.MatchEntry
import kotlinx.coroutines.flow.Flow

class RoomLocalMatchDataSource(private val matchesDao: MatchesDao) : LocalMatchDataSource {
    override suspend fun matches(): Flow<List<MatchEntry>> {
        return matchesDao.matches()
    }

    override suspend fun saveMatch(match: MatchEntry) {
        matchesDao.insertOrUpdate(match)
    }
}
