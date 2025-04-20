package com.padabajka.dating.feature.messenger.presentation.chat.model.item

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
            .sortedByDescending { (date, _) -> date }

        for ((date, messagesList) in dateGroups) {
            addAll(messagesList)
            add(TimeItem.getTimeItem(date))
        }
    }.toPersistentList()
