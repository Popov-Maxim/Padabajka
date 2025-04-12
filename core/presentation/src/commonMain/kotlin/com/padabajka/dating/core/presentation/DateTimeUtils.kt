package com.padabajka.dating.core.presentation

import kotlinx.datetime.LocalDateTime

@Suppress("ImplicitDefaultLocale")
val LocalDateTime.hourMinutes: String
    get() = "$hour:${minute.twoDigits}"

private val Int.twoDigits: String
    get() = toString().padStart(2, '0')
