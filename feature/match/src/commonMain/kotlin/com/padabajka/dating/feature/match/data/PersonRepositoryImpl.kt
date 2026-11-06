package com.padabajka.dating.feature.match.data

import com.padabajka.dating.component.room.person.entry.PersonEntry
import com.padabajka.dating.core.domain.retryLoading
import com.padabajka.dating.core.presentation.error.DomainErrorHandler
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.core.utils.runCatchingCancellable
import com.padabajka.dating.feature.match.data.source.local.LocalPersonDataSource
import com.padabajka.dating.feature.match.data.source.local.toEntry
import com.padabajka.dating.feature.match.data.source.local.toPerson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

class PersonRepositoryImpl(
    private val localPersonDataSource: LocalPersonDataSource,
    private val profileRepository: ProfileRepository,
    private val domainErrorHandler: DomainErrorHandler
) : PersonRepository {
    override fun observePerson(personId: PersonId): Flow<Person?> = flow {
        val cachePerson = localPersonDataSource.getPerson(personId)
        emit(cachePerson?.toPerson())
        if (cachePerson?.isFresh() != true) {
            emit(refreshPerson(personId))
        }
    }.retryLoading().distinctUntilChanged()

    override suspend fun getPerson(personId: PersonId): Person? {
        val cached = localPersonDataSource.getPerson(personId) ?: return refreshPerson(personId)
        refreshIfNeed(personId, cached)

        return cached.toPerson()
    }

    private suspend fun refreshIfNeed(personId: PersonId, cached: PersonEntry) {
        if (cached.isFresh().not()) {
            CoroutineScope(currentCoroutineContext()).launch {
                runCatchingCancellable { refreshPerson(personId) }
                    .onFailure { domainErrorHandler.handleExternal(it) }
            }
        }
    }

    private suspend fun refreshPerson(personId: PersonId): Person? {
        val profile = profileRepository.profile(personId)
        if (profile == null) {
            localPersonDataSource.deletePerson(personId)
            return null
        }
        val person = Person(personId, profile)
        savePerson(person)
        return person
    }

    override suspend fun savePerson(person: Person) {
        val now = Clock.System.now().toEpochMilliseconds()
        localPersonDataSource.savePerson(person.toEntry(now))
    }

    private fun PersonEntry.isFresh(): Boolean {
        val age = Clock.System.now().toEpochMilliseconds() - updatedAt

        return age in 0 until CACHE_TTL.inWholeMilliseconds
    }
    private companion object {
        private val CACHE_TTL = 10.minutes
    }
}
