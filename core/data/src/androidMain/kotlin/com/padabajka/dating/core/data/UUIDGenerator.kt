package com.padabajka.dating.core.data

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()
