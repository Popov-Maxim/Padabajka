package com.padabajka.dating.core.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_MD5
import platform.CoreCrypto.CC_MD5_DIGEST_LENGTH
import platform.Foundation.NSUUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

actual fun uuid(): String = NSUUID().UUIDString()

@OptIn(ExperimentalUuidApi::class)
actual fun uuidFromKey(key: String): String {
    return nameUUIDFromBytesIos(key.encodeToByteArray()).toString()
}

@OptIn(ExperimentalUuidApi::class, ExperimentalForeignApi::class)
@Suppress("MagicNumber")
private fun nameUUIDFromBytesIos(bytes: ByteArray): Uuid {
    val digest = ByteArray(CC_MD5_DIGEST_LENGTH)

    // 1. Вычисляем MD5 средствами iOS
    bytes.usePinned { pinnedBytes ->
        digest.usePinned { pinnedDigest ->
            CC_MD5(pinnedBytes.addressOf(0), bytes.size.toUInt(), pinnedDigest.addressOf(0).reinterpret())
        }
    }

    // 2. Выставляем версию 3 (в 6-м байте, индекс 6)
    digest[6] = ((digest[6].toInt() and 0x0F) or 0x30).toByte()

    // 3. Выставляем вариант RFC 4122 (в 8-м байте, индекс 8)
    digest[8] = ((digest[8].toInt() and 0x3F) or 0x80).toByte()

    return Uuid.fromByteArray(digest)
}
