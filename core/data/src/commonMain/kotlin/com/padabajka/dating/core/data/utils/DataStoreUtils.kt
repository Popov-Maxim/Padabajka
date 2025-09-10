package com.padabajka.dating.core.data.utils

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.serialization.KSerializer
import okio.Path.Companion.toPath

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

    fun <T> create(dbName: String, delegate: KSerializer<T>, default: T): DataStore<T> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = PathUtils.fileSystem,
                serializer = OkioSerializerWrapper(delegate, default),
                producePath = { PathUtils.getAbsolutePath(dbName).toPath() }
            )
        )
    }
}
