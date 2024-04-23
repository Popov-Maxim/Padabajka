package com.fp.padabajka.feature.swiper.data.person

import com.fp.padabajka.core.data.Atomic
import com.fp.padabajka.core.data.MutableAtomic
import com.fp.padabajka.core.data.atomic
import com.fp.padabajka.core.data.mutableAtomic
import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.Person
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.person.source.RemotePersonDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PersonRepositoryImpl(
    private val scope: CoroutineScope,
    private val remotePersonDataSource: RemotePersonDataSource
) : PersonRepository {

    private var actualSearchPreferences: MutableAtomic<SearchPreferences?> = mutableAtomic(null)

    private val sharedPersons: Atomic<MutableSet<Person>> = atomic(mutableSetOf())

    private val preloadedPersons: Atomic<MutableMap<SearchPreferences, ArrayDeque<Person>>> =
        atomic(mutableMapOf())

    private var preloadJobs: Atomic<MutableMap<SearchPreferences, Deferred<Unit>>> = atomic(
        mutableMapOf()
    )

    private val personObtainMutex = Mutex()

    override suspend fun getPerson(searchPreferences: SearchPreferences): Person? {
        updateSearchPreferences(searchPreferences)
        return obtainPerson(searchPreferences)
    }

    // TODO: Rename me, please:(
    override suspend fun setUsed(personId: PersonId) {
        sharedPersons {
            removeAll { it.id == personId }
        }
    }

    private suspend fun loadMorePersonsIfNeeded(searchPreferences: SearchPreferences) {
        if (preloadedPersons { size < MIN_CAPACITY }) {
            preloadJobs {
                if (get(searchPreferences)?.isActive != true) {
                    this[searchPreferences] = scope.async {
                        preloadPersons(searchPreferences)
                    }
                }
            }
        }
    }

    private suspend fun obtainPerson(searchPreferences: SearchPreferences): Person? {
        personObtainMutex.withLock {
            ensureActual(searchPreferences)

            loadMorePersonsIfNeeded(searchPreferences)

            if (preloadedPersons { get(searchPreferences).isNullOrEmpty() }) {
                preloadJobs { get(searchPreferences) }?.await()
                    ?: error("Persons is empty but preloadJob is null!")
            }

            val person = preloadedPersons { get(searchPreferences)?.removeFirstOrNull() }
            if (person != null) {
                sharedPersons { add(person) }
            }
            return person
        }
    }

    private suspend fun preloadPersons(searchPreferences: SearchPreferences) {
        ensureActual(searchPreferences)
        val preloaded = preloadedPersons { get(searchPreferences)?.toSet() } ?: emptySet()
        val shared = sharedPersons { toSet() }

        val loaded = shared + preloaded

        val newPersons = remotePersonDataSource.getPersons(LOADING_COUNT, loaded, searchPreferences)
        if (newPersons.isNotEmpty()) {
            preloadedPersons {
                if (containsKey(searchPreferences)) {
                    getValue(searchPreferences).addAll(newPersons)
                } else {
                    put(searchPreferences, ArrayDeque(newPersons))
                }
            }
        }
    }

    private suspend fun updateSearchPreferences(searchPreferences: SearchPreferences) =
        actualSearchPreferences.update {
            if (this != searchPreferences) {
                preloadJobs {
                    values.forEach { preloadJob ->
                        if (preloadJob.isActive) {
                            preloadJob.cancel()
                        }
                    }
                    clear()
                }
                preloadedPersons { clear() }
                sharedPersons { clear() }
            }
            searchPreferences
        }

    private suspend fun ensureActual(searchPreferences: SearchPreferences) =
        coroutineScope {
            this@PersonRepositoryImpl.actualSearchPreferences {
                if (this == searchPreferences) {
                    cancel("Search preferences got updated!")
                }
            }
        }

    companion object {
        private const val LOADING_COUNT = 10
        private const val MIN_CAPACITY = 4
    }
}
