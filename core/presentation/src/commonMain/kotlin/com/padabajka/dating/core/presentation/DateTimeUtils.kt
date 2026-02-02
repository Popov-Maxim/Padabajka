package com.padabajka.dating.core.presentation

import androidx.compose.runtime.Stable
import com.padabajka.dating.core.utils.now
import kotlinx.datetime.LocalDateTime

@Suppress("ImplicitDefaultLocale")
val LocalDateTime.hourMinutes: String
    get() = "$hour:${minute.twoDigits}"

private val Int.twoDigits: String
    get() = toString().padStart(2, '0')

private val LocalDateTime.dayMonth: String
    get() = "${dayOfMonth.twoDigits}.${monthNumber.twoDigits}"

private val LocalDateTime.dayMonthYear: String
    get() = "${dayOfMonth.twoDigits}.${monthNumber.twoDigits}.$year"

private fun LocalDateTime.formatEditedTime(now: LocalDateTime): String =
    when {
        date == now.date ->
            hourMinutes

        year == now.year ->
            "$hourMinutes, $dayMonth"

        else ->
            "$hourMinutes, $dayMonthYear"
    }

@Stable
val LocalDateTime.compactText: String // TODO: improve for english
    get() {
        val a = LocalDateTime.now()
        return formatEditedTime(a)
    }
