package com.padabajka.dating.feature.match.data

import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.match.RawMatch
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.match.data.model.toEntry
import com.padabajka.dating.feature.match.data.source.local.LocalMatchDataSource
import com.padabajka.dating.feature.match.data.source.local.toEntry
import com.padabajka.dating.feature.match.data.source.local.toMatch
import com.padabajka.dating.feature.match.data.source.local.toRawMatch
import com.padabajka.dating.feature.match.data.source.remote.RemoteMatchDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MatchRepositoryImpl(
    private val localMatchDataSource: LocalMatchDataSource,
    private val remoteMatchDataSource: RemoteMatchDataSource,
    private val personRepository: PersonRepository
) : MatchRepository {
    override fun rawMatches(): Flow<List<RawMatch>> = localMatchDataSource.matches().map { matches ->
        matches.map { match ->
            match.toRawMatch()
        }
    }.distinctUntilChanged()

    override fun matchedPersonIds(): Flow<Set<PersonId>> =
        localMatchDataSource.matches()
            .map { matches ->
                matches.mapTo(mutableSetOf()) { PersonId(it.personId) }
            }
            .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun findMatch(chatId: ChatId): Flow<Match?> {
        return localMatchDataSource.findMatch(chatId).flatMapLatest { match ->
            if (match == null) {
                flowOf(null)
            } else {
                personRepository.observePerson(PersonId(match.personId))
                    .map { person -> match.toMatch(person) }
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
