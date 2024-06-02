package com.fp.padabajka.feature.messenger.presentation.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

fun currentDateTime(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun currentDate(): LocalDate = currentDateTime().date

fun yesterdayDate(): LocalDate = currentDate().minus(DatePeriod(days = 1))
