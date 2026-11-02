package com.padabajka.dating.core.utils

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()

actual fun uuidFromKey(key: String): String {
    return UUID.nameUUIDFromBytes(key.toByteArray(Charsets.UTF_8)).toString()
}
