package com.padabajka.dating.feature.match.data

import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.match.data.source.local.LocalPersonDataSource
import com.padabajka.dating.feature.match.data.source.local.toEntry
import com.padabajka.dating.feature.match.data.source.local.toPerson

class PersonRepositoryImpl(
    private val localPersonDataSource: LocalPersonDataSource,
    private val profileRepository: ProfileRepository
) : PersonRepository {
    override suspend fun getPerson(personId: PersonId): Person {
        return localPersonDataSource.getPerson(personId)?.toPerson() ?: run {
            val profile = profileRepository.profile(personId)
            val person = Person(personId, profile)
            savePerson(person)
            person
        }
    }

    override suspend fun savePerson(person: Person) {
        return localPersonDataSource.savePerson(person.toEntry())
    }
}
