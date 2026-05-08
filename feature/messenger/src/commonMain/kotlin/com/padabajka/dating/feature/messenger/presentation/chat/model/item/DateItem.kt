package com.padabajka.dating.feature.messenger.presentation.chat.model.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.compactText
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.messenger.presentation.chat.util.currentDate
import com.padabajka.dating.feature.messenger.presentation.chat.util.yesterdayDate
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

@Immutable
open class StringTimeItem(private val text: StaticTextId) : TimeItem {
    @Composable
    override fun labelText(): String = text.translate()

    override val key: Any
        get() = text.hashCode()
}

data object YesterdayTimeItem : StringTimeItem(StaticTextId.UiId.Yesterday)
data object TodayTimeItem : StringTimeItem(StaticTextId.UiId.Today)

class DateTimeItem(private val date: LocalDate) : TimeItem {

    override val key: Any
        get() = date.hashCode()

    @Composable
    override fun labelText(): String {
        return date.compactText()
    }
}
