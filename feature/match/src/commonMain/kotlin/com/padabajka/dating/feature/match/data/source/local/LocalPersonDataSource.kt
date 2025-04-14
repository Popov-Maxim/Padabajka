package com.padabajka.dating.feature.match.data.source.local

import com.padabajka.dating.component.room.person.entry.PersonEntry
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

interface LocalPersonDataSource {
    suspend fun getPerson(personId: PersonId): PersonEntry?
    suspend fun savePerson(person: PersonEntry)
}
