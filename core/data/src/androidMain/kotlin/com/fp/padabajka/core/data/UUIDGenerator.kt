package com.fp.padabajka.core.data

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()
