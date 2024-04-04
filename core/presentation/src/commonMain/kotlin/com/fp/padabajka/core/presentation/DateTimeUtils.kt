package com.fp.padabajka.core.presentation

import kotlinx.datetime.LocalDateTime

val LocalDateTime.hourMinutes: String
    get() = "$hour:$minute"
