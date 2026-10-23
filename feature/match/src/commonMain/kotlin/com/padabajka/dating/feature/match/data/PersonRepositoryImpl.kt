package com.padabajka.dating.feature.match.data

import com.padabajka.dating.core.domain.retryLoading
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.match.data.source.local.LocalPersonDataSource
import com.padabajka.dating.feature.match.data.source.local.toEntry
import com.padabajka.dating.feature.match.data.source.local.toPerson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class PersonRepositoryImpl(
    private val localPersonDataSource: LocalPersonDataSource,
    private val profileRepository: ProfileRepository
) : PersonRepository {
    override fun observePerson(personId: PersonId): Flow<Person?> = flow {
        val cachePerson = localPersonDataSource.getPerson(personId)?.toPerson()
        emit(cachePerson)
        if (cachePerson == null) {
            emit(loadAndSavePerson(personId))
        }
    }.retryLoading().distinctUntilChanged()

    override suspend fun getPerson(personId: PersonId): Person? {
        return localPersonDataSource.getPerson(personId)?.toPerson()
            ?: loadAndSavePerson(personId)
    }

    private suspend fun loadAndSavePerson(personId: PersonId): Person? {
        val profile = profileRepository.profile(personId) ?: return null
        val person = Person(personId, profile)
        savePerson(person)
        return person
    }

    override suspend fun savePerson(person: Person) {
        return localPersonDataSource.savePerson(person.toEntry())
    }
}
