package com.fp.padabajka.core.data

import platform.Foundation.NSUUID

actual fun uuid(): String = NSUUID().UUIDString()
