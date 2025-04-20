package com.padabajka.dating.feature.messenger.presentation.chat.model.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.padabajka.dating.feature.messenger.presentation.chat.util.currentDate
import com.padabajka.dating.feature.messenger.presentation.chat.util.yesterdayDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

@Immutable
interface TimeItem : MessengerItem {
    @Composable
    fun labelText(): String

    companion object {
        fun getTimeItem(date: LocalDate): TimeItem {
            return when (date) {
                currentDate() -> TodayTimeItem
                yesterdayDate() -> YesterdayTimeItem
                else -> DateTimeItem(date)
            }
        }
    }
}

// TODO: Add resources handling
@Immutable
open class StringTimeItem(private val text: String) : TimeItem {
    @Composable
    override fun labelText(): String = text

    override val key: Any
        get() = text.hashCode()
}

// TODO: Remove strings hardcode
data object YesterdayTimeItem : StringTimeItem("Yesterday")
data object TodayTimeItem : StringTimeItem("Today")

class DateTimeItem(private val date: LocalDate) : TimeItem {

    override val key: Any
        get() = date.hashCode()

    @Composable
    override fun labelText(): String {
        val format = LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth()
            chars(", ")
            year()
        }
        return date.format(format)
    }
}
