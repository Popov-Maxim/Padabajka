package com.padabajka.dating.core.data.utils

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

object DataStoreUtils {
    fun <T> createFake(initValue: T): DataStore<T> {
        return object : DataStore<T> {
            private val _data = MutableStateFlow(initValue)
            override val data: Flow<T> = _data

            override suspend fun updateData(
                transform: suspend (t: T) -> T
            ): T {
                return _data.updateAndGet { transform(it) }
            }
        } // TODO(datastore): add init
    }
}
