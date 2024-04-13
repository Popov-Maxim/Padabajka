package com.fp.padabajka.feature.swiper.data.person

import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.person.source.RemotePersonDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PersonRepositoryImpl(
    private val scope: CoroutineScope,
    private val remotePersonDataSource: RemotePersonDataSource
) : PersonRepository {

    private var actualSearchPreferences: SearchPreferences? = null

    private val persons = ArrayDeque<Person>()

    private var preloadJob: Deferred<Unit>? = null

    private val mutex = Mutex()

    private val personsChangeMutex = Mutex()

    private val searchPreferencesChangeMutex = Mutex()

    override suspend fun getPerson(searchPreferences: SearchPreferences): Person = coroutineScope {
        updateSearchPreferences(searchPreferences)
        if (persons.size < MIN_CAPACITY) {
            if (preloadJob?.isActive != true) {
                preloadJob = scope.async {
                    preloadPersons(searchPreferences)
                    ensureActive()
                    preloadJob = null
                }
            }
        }

        mutex.withLock {
            if (searchPreferences != actualSearchPreferences) {
                cancel("SearchPreferences had changed!")
            }

            if (persons.isEmpty()) {
                preloadJob?.await() ?: throw TODO()
            }

            personsChangeMutex.withLock {
                return@coroutineScope persons.removeFirst()
            }
        }
    }

    private suspend fun preloadPersons(searchPreferences: SearchPreferences) {
        val newPersons = remotePersonDataSource.getPersons(LOADING_COUNT, searchPreferences)
        if (newPersons.isEmpty()) {
            throw TODO()
        } else {
            personsChangeMutex.withLock {
                persons.addAll(newPersons)
            }
        }
    }

    private suspend fun updateSearchPreferences(searchPreferences: SearchPreferences) {
        searchPreferencesChangeMutex.withLock {
            if (actualSearchPreferences != searchPreferences) {
                actualSearchPreferences = searchPreferences
                preloadJob?.cancel()
                preloadJob = null
                personsChangeMutex.withLock {
                    persons.clear()
                }
            }
        }
    }

    companion object {
        private const val LOADING_COUNT = 10
        private const val MIN_CAPACITY = 4
    }
}
