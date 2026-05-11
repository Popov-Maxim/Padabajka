package com.padabajka.dating.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.padabajka.dating.core.presentation.ui.dictionary.languageState
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.utils.now
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@Suppress("ImplicitDefaultLocale")
val LocalDateTime.hourMinutes: String
    get() = "$hour:${minute.twoDigits}"

private val Int.twoDigits: String
    get() = toString().padStart(2, '0')

@Composable
private fun LocalDateTime.formatEditedTime(now: LocalDateTime): String =
    when {
        date == now.date ->
            hourMinutes

        else ->
            "$hourMinutes, ${date.compactText(false)}"
    }

@Stable
val LocalDateTime.compactText: String
    @Composable
    get() {
        val a = LocalDateTime.now()
        return formatEditedTime(a)
    }

private object CoreMonthNames {
    val RUS_ABBREVIATED: MonthNames = MonthNames(
        listOf(
            "янв", "фев", "мар", "апр", "мая", "июн",
            "июл", "авг", "сен", "окт", "ноя", "дек"
        )
    )
    val RUS_FULL: MonthNames = MonthNames(
        listOf(
            "января",
            "февраля",
            "марта",
            "апреля",
            "мая",
            "июня",
            "июля",
            "августа",
            "сентября",
            "октября",
            "ноября",
            "декабря"
        )
    )
}

private fun monthNames(language: Language): MonthNames {
    return when (language) {
        is Language.Dynamic,
        Language.Static.EN -> MonthNames.ENGLISH_ABBREVIATED

        Language.Static.RU -> CoreMonthNames.RUS_ABBREVIATED
    }
}

private fun fullMonthNames(language: Language): MonthNames {
    return when (language) {
        is Language.Dynamic,
        Language.Static.EN -> MonthNames.ENGLISH_FULL

        Language.Static.RU -> CoreMonthNames.RUS_FULL
    }
}

@Stable
@Composable
fun LocalDate.compactText(abbreviatedMonthNames: Boolean = false): String {
    val language by languageState()
    val monthNames = if (abbreviatedMonthNames) {
        monthNames(language)
    } else {
        fullMonthNames(language)
    }
    val currentYear = LocalDateTime.now()
    val showYear = year != currentYear.year

    val format = LocalDate.Format {
        when (language) {
            Language.Static.RU -> {
                dayOfMonth()
                char(' ')
                monthName(monthNames)

                if (showYear) {
                    char(' ')
                    year()
                }
            }

            Language.Static.EN -> {
                monthName(monthNames)
                char(' ')
                dayOfMonth()

                if (showYear) {
                    chars(", ")
                    year()
                }
            }
        }
    }

    return format(format)
}

private fun LocalDateTime.Companion.fromMillis(millis: Long): LocalDateTime =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault())

@Composable
fun rememberLocalDateTime(millis: Long): String {
    return remember(millis) { LocalDateTime.fromMillis(millis).hourMinutes }
}
