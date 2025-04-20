package com.padabajka.dating.feature.messenger.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.ProfileAvatar
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.messenger.presentation.model.ChatItem
import com.padabajka.dating.feature.messenger.presentation.model.EmptyChatItem
import com.padabajka.dating.feature.messenger.presentation.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.model.OpenChatEvent

@Composable
fun MessengerScreen(component: MessengerComponent, navigateBar: @Composable () -> Unit) {
    CustomScaffold(
        bottomBar = navigateBar
    ) {
        Content(component)
    }
}

@Composable
private fun Content(component: MessengerComponent) {
    val state by component.state.subscribeAsState()

    Column {
        MatchesBlock(state.matchesWithoutChat, component::onEvent)
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Black
        )
        ChatsBlock(state.chats, component::onEvent)
    }
}

@Composable
private fun MatchesBlock(matches: List<EmptyChatItem>, onEvent: (MessengerEvent) -> Unit) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = StaticTextId.UiId.Matches.translate(),
                fontFamily = PlayfairDisplay,
                fontSize = 30.sp
            )
        }

        LazyRow(
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(matches) { match ->
                ProfileAvatar(match.match.person.image.raw()) {
                    onEvent(OpenChatEvent(match.chatId, match.match.person))
                }
            }
        }
    }
}

@Composable
private fun ChatsBlock(chats: List<ChatItem>, onEvent: (MessengerEvent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = StaticTextId.UiId.Chats.translate(),
            fontFamily = PlayfairDisplay,
            fontSize = 30.sp
        )
    }

    LazyColumn {
        itemsIndexed(chats) { index, chat ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onEvent(OpenChatEvent(chat.chatId, chat.match.person))
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    ProfileAvatar(chat.match.person.image.raw())
                }
                Column {
                    Text(
                        text = chat.match.person.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = chat.lastMessage.content,
                        fontSize = 16.sp
                    )
                }
            }
            if (index != chats.lastIndex) {
                Box(Modifier.padding(horizontal = 10.dp)) {
                    HorizontalDivider(
                        thickness = 0.2.dp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
