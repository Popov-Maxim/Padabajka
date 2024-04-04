package com.fp.padabajka.feature.ads.yandex

import platform.Foundation.NSNumber

fun Int?.toNSNumber(): NSNumber? = this?.let { NSNumber(it) }
