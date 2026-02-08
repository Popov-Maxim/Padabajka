package com.padabajka.dating.feature.match.data

import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.match.RawMatch
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.match.data.model.toEntry
import com.padabajka.dating.feature.match.data.source.local.LocalMatchDataSource
import com.padabajka.dating.feature.match.data.source.local.toEntry
import com.padabajka.dating.feature.match.data.source.local.toMatch
import com.padabajka.dating.feature.match.data.source.remote.RemoteMatchDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MatchRepositoryImpl(
    private val localMatchDataSource: LocalMatchDataSource,
    private val remoteMatchDataSource: RemoteMatchDataSource,
    private val personRepository: PersonRepository
) : MatchRepository {
    override suspend fun matches(): Flow<List<Match>> {
        return localMatchDataSource.matches().map {
            it.mapNotNull { match ->
                val personId = PersonId(match.personId)
                val person = personRepository.getPerson(personId) ?: return@mapNotNull null
                match.toMatch(person)
            }
        }
    }

    override suspend fun saveMatch(match: RawMatch) {
        localMatchDataSource.saveMatch(match.toEntry())
    }

    override suspend fun sync() {
        val matchesDto = remoteMatchDataSource.loadMatches()
        val matches = matchesDto.map { it.toEntry() }
        localMatchDataSource.replaceMatches(matches)
    }

    override suspend fun deleteMatch(match: Match.Id) {
        remoteMatchDataSource.deleteMatch(match.raw)
    }
}
