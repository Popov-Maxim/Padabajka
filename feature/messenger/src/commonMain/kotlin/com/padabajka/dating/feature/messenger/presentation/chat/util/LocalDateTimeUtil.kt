package com.padabajka.dating.feature.messenger.presentation.chat.util

import com.padabajka.dating.core.utils.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus

fun currentDateTime(): LocalDateTime = LocalDateTime.now()

fun currentDate(): LocalDate = currentDateTime().date

fun yesterdayDate(): LocalDate = currentDate().minus(DatePeriod(days = 1))
