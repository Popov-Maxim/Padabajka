package com.padabajka.dating.feature.messenger.presentation.chat.ui

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.feature.messenger.presentation.chat.model.ChatLoadingState
import com.padabajka.dating.feature.messenger.presentation.chat.model.EndOfMessagesListReachedEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.IncomingMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessengerItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.TimeItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

// TODO: Remane
@Suppress("UnusedParameter")
@Composable
fun Chat(
    modifier: Modifier = Modifier,
    items: PersistentList<MessengerItem>,
    followNewItems: MutableState<Boolean>,
    loadingState: ChatLoadingState,
    onEvent: (MessengerEvent) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollScope = rememberCoroutineScope()

    val unreadMessagesCount by rememberUpdatedState(
        items.filterIsInstance<IncomingMessageItem>()
            .count { it.hasBeenRead.not() }
    )

    LaunchedEffect(listState) {
        listState.interactionSource.interactions
            .filterIsInstance<DragInteraction>()
            .collectLatest {
                followNewItems.value = listState.firstVisibleItemIndex == 0
            }
    }
    Box(modifier) {
        MessageList(
            messengerItems = items,
            listState = listState,
            followNewItems = followNewItems,
            onEvent = onEvent
        )
        ArrowDownCounterButton(
            count = unreadMessagesCount,
            visible = unreadMessagesCount != 0,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            val lastUnreadIndex = items.indexOfLast {
                (it as? IncomingMessageItem)?.hasBeenRead == false
            }

            scrollScope.launch {
                if (listState.firstVisibleItemIndex <= lastUnreadIndex + 1 || lastUnreadIndex == 0) {
                    listState.animateScrollToItem(0)
                    followNewItems.value = true
                } else {
                    listState.animateScrollToItem(lastUnreadIndex)
                }
            }
        }
    }
}

@Composable
private fun MessageList(
    messengerItems: PersistentList<MessengerItem>,
    listState: LazyListState,
    followNewItems: MutableState<Boolean>,
    onEvent: (MessengerEvent) -> Unit
) {
    LaunchedEffect(messengerItems.firstOrNull()) {
        if (followNewItems.value) {
            listState.animateScrollToItem(index = 0)
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        state = listState,
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(7.dp, alignment = Alignment.Bottom)
    ) {
        itemsIndexed(
            items = messengerItems,
            key = { _, item -> item.key }
        ) { index, item ->
            when (item) {
                is MessageItem -> Message(
                    message = item,
                    shape = RoundedCornerShape(15.dp),
                    onEvent = onEvent
                )
                is TimeItem -> TimePeriod(item.labelText())
            }
            if (index == messengerItems.lastIndex) {
                LaunchedEffect(item) {
                    onEvent(EndOfMessagesListReachedEvent)
                }
            } else {
//                MessengerSpacer(messengerItems[index + 1], item)
            }
        }
    }
}
