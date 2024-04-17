package com.fp.padabajka.feature.swiper.data.person

import com.fp.padabajka.core.data.Atomic
import com.fp.padabajka.core.data.MutableAtomic
import com.fp.padabajka.core.data.atomic
import com.fp.padabajka.core.data.mutableAtomic
import com.fp.padabajka.core.repository.api.PersonRepository
import com.fp.padabajka.core.repository.api.model.swiper.Person
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

    private val persons: Atomic<ArrayDeque<Person>> =
        atomic(ArrayDeque(LOADING_COUNT + MIN_CAPACITY))

    private var preloadJob: Deferred<Unit>? = null

    private val personObtainMutex = Mutex()

    override suspend fun getPerson(searchPreferences: SearchPreferences): Person? {
        updateSearchPreferences(searchPreferences)
        loadMorePersonsIfNeeded(searchPreferences)
        return obtainPerson(searchPreferences)
    }

    private suspend fun loadMorePersonsIfNeeded(searchPreferences: SearchPreferences) {
        if (persons { size } < MIN_CAPACITY) {
            if (preloadJob?.isActive != true) {
                preloadJob = scope.async {
                    preloadPersons(searchPreferences)
                }
            }
        }
    }

    private suspend fun obtainPerson(searchPreferences: SearchPreferences): Person? =
        coroutineScope {
            personObtainMutex.withLock {
                ensureActual(searchPreferences)

                if (persons { isEmpty() }) {
                    preloadJob?.await()
                        ?: throw IllegalStateException("Persons is empty but preloadJob is null!")
                }

                return@coroutineScope persons { removeFirstOrNull() }
            }
        }

    private suspend fun preloadPersons(searchPreferences: SearchPreferences) = coroutineScope {
        ensureActual(searchPreferences)

        val newPersons = remotePersonDataSource.getPersons(LOADING_COUNT, searchPreferences)
        if (newPersons.isNotEmpty()) {
            persons { addAll(newPersons) }
        }
    }

    private suspend fun updateSearchPreferences(searchPreferences: SearchPreferences) =
        actualSearchPreferences.update {
            if (this != searchPreferences) {
                if (preloadJob?.isActive == true) {
                    preloadJob?.cancel()
                }
                persons { clear() }
            }
            searchPreferences
        }

    private suspend fun CoroutineScope.ensureActual(searchPreferences: SearchPreferences) =
        actualSearchPreferences {
            if (this == searchPreferences) {
                cancel("Search preferences got updated!")
            }
        }

    companion object {
        private const val LOADING_COUNT = 10
        private const val MIN_CAPACITY = 4
    }
}
