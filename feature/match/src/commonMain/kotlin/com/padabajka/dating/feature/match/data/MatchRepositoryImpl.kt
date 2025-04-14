package com.padabajka.dating.feature.match.data

import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.match.data.source.local.LocalMatchDataSource
import com.padabajka.dating.feature.match.data.source.local.toEntry
import com.padabajka.dating.feature.match.data.source.local.toMatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MatchRepositoryImpl(
    private val localMatchDataSource: LocalMatchDataSource,
    private val personRepository: PersonRepository
) : MatchRepository {
    override suspend fun matches(): Flow<List<Match>> {
        return localMatchDataSource.matches().map {
            it.map { match ->
                val personId = PersonId(match.personId)
                val person = personRepository.getPerson(personId)
                match.toMatch(person)
            }
        }
    }

    override suspend fun saveMatch(match: Match) {
        localMatchDataSource.saveMatch(match.toEntry())
    }
}
