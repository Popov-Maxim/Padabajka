package com.fp.padabajka.feature.ads.data

import platform.Foundation.NSNumber

fun Int?.toNSNumber(): NSNumber? = this?.let { NSNumber(it) }
