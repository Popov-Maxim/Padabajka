package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.matches.MatchesDao
import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.core.repository.api.model.auth.UserId
import kotlinx.coroutines.flow.Flow

class RoomLocalMatchDataSource(private val matchesDao: MatchesDao) : LocalMatchDataSource {
    override fun matches(): Flow<List<MatchEntry>> {
        return matchesDao.matches()
    }

    override suspend fun saveMatch(match: MatchEntry) {
        matchesDao.insertOrUpdate(match)
    }

    override suspend fun replaceMatches(matches: List<MatchEntry>) {
        matchesDao.replaceMatchesForUser(UserId(""), matches)
    }
}
