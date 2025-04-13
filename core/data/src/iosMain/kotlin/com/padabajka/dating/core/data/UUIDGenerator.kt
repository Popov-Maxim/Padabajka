package com.padabajka.dating.core.data

import platform.Foundation.NSUUID

actual fun uuid(): String = NSUUID().UUIDString()
