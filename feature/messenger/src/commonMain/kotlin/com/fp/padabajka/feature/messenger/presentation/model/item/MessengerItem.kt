package com.fp.padabajka.feature.messenger.presentation.model.item

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

sealed interface MessengerItem {
    val key: Any
}

fun List<MessageItem>.addDateItems(): PersistentList<MessengerItem> =
    buildList {
        if (this@addDateItems.isEmpty()) {
            return@buildList
        }

        val dateGroups = this@addDateItems.groupBy { it.sentTime.date }
            .entries
            .sortedBy { (date, _) -> date }

        for ((date, messagesList) in dateGroups.dropLast(1)) {
            addAll(messagesList)
            add(TimeItem.getTimeItem(date))
        }
        addAll(dateGroups.last().value)
    }.toPersistentList()
