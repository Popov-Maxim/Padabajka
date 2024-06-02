package com.fp.padabajka.feature.messenger.presentation.element

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fp.padabajka.feature.messenger.presentation.model.ChatLoadingState
import com.fp.padabajka.feature.messenger.presentation.model.EndOfMessagesListReachedEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerEvent
import com.fp.padabajka.feature.messenger.presentation.model.item.IncomingMessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.MessengerItem
import com.fp.padabajka.feature.messenger.presentation.model.item.OutgoingMessageItem
import com.fp.padabajka.feature.messenger.presentation.model.item.TimeItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

// TODO: Remane
@Suppress("UnusedParameter")
@Composable
fun Chat(
    modifier: Modifier = Modifier,
    items: () -> PersistentList<MessengerItem>,
    followNewItems: MutableState<Boolean>,
    loadingState: () -> ChatLoadingState,
    onEvent: (MessengerEvent) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollScope = rememberCoroutineScope()

    val itemsList = items()
    val unreadMessagesCount by rememberUpdatedState(
        itemsList.filterIsInstance<IncomingMessageItem>()
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
            messengerItems = itemsList,
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
            val lastUnreadIndex = itemsList.indexOfLast {
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
fun MessageList(
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
        state = listState,
        reverseLayout = true
    ) {
        itemsIndexed(
            items = messengerItems,
            key = { _, item -> item.key }
        ) { index, item ->
            when (item) {
                is IncomingMessageItem -> IncomingMessage(item, onEvent)
                is OutgoingMessageItem -> OutgoingMessage(item, onEvent)
                is TimeItem -> TimePeriod(item.labelText())
            }
            if (index == messengerItems.lastIndex) {
                LaunchedEffect(item) {
                    onEvent(EndOfMessagesListReachedEvent)
                }
            } else {
                MessengerSpacer(messengerItems[index + 1], item)
            }
        }
    }
}
