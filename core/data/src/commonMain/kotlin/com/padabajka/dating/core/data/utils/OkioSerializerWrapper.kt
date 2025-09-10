package com.padabajka.dating.core.data.utils

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

internal class OkioSerializerWrapper<T>(private val delegate: KSerializer<T>, default: T) : OkioSerializer<T> {
    override val defaultValue: T = default

    override suspend fun readFrom(source: BufferedSource): T {
        val bytes = source.readByteArray()
        return Json.decodeFromString(delegate, bytes.decodeToString())
    }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        val bytes = Json.encodeToString(delegate, t).encodeToByteArray()
        sink.write(bytes)
    }
}
