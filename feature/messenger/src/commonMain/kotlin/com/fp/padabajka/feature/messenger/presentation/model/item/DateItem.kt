package com.fp.padabajka.feature.messenger.presentation.model.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.fp.padabajka.feature.messenger.presentation.util.currentDate
import com.fp.padabajka.feature.messenger.presentation.util.yesterdayDate
import kotlinx.datetime.LocalDate

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
        return "${date.dayOfMonth}:${date.monthNumber}"
    }
}