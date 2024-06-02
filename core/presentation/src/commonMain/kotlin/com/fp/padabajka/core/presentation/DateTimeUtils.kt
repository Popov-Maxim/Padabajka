package com.fp.padabajka.core.presentation

import kotlinx.datetime.LocalDateTime

@Suppress("ImplicitDefaultLocale")
val LocalDateTime.hourMinutes: String
    get() = String.format("%d:%02d", hour, minute)
