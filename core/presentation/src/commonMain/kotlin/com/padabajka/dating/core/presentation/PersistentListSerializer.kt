package com.padabajka.dating.core.presentation

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PersistentList::class)
class PersistentListSerializer<T>(
    private val dataSerializer: KSerializer<T>
) : KSerializer<PersistentList<T>> {

    override val descriptor: SerialDescriptor =
        ListSerializer(dataSerializer).descriptor
    override fun serialize(encoder: Encoder, value: PersistentList<T>) {
        return ListSerializer(dataSerializer).serialize(encoder, value.toList())
    }
    override fun deserialize(decoder: Decoder): PersistentList<T> {
        return ListSerializer(dataSerializer).deserialize(decoder).toPersistentList()
    }
}
