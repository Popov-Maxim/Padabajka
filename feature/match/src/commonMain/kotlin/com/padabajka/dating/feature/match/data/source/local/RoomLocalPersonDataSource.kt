package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.person.PersonDao
import com.padabajka.dating.component.room.person.entry.PersonEntry
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

class RoomLocalPersonDataSource(
    private val personDao: PersonDao
) : LocalPersonDataSource {
    override suspend fun getPerson(personId: PersonId): PersonEntry {
        return personDao.getPerson(personId.raw)
    }

    override suspend fun savePerson(person: PersonEntry) {
        personDao.insertOrUpdate(person)
    }
}
