package com.padabajka.dating.feature.messenger.presentation.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.ProfileAvatar
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.messenger.presentation.chat.model.DeleteChatEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.DeleteMatchEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.Field
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NavigateBackEvent
import com.padabajka.dating.feature.messenger.presentation.chat.ui.Chat
import com.padabajka.dating.feature.messenger.presentation.chat.ui.EditedMessageField
import com.padabajka.dating.feature.messenger.presentation.chat.ui.MessageField
import com.padabajka.dating.feature.messenger.presentation.chat.ui.ParentMessageField
import com.padabajka.dating.feature.messenger.presentation.model.PersonItem
import com.padabajka.dating.feature.messenger.presentation.model.UserPresenceItem
import com.padabajka.dating.feature.messenger.presentation.model.text
import com.padabajka.dating.feature.messenger.presentation.model.toPersonView
import com.padabajka.dating.feature.profile.presentation.ProfileViewBottomSheet
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewMode

@Composable
fun ChatScreen(component: ChatComponent) {
    val state by component.state.subscribeAsState()
    val followNewItems = remember {
        mutableStateOf(true)
    }

    CustomScaffold(
        topBar = {
            TopBar(state.person, state.userPresence, component::onEvent)
        },
        bottomBar = {
            Column {
                val field = state.field
                if (field is Field.Editor) {
                    EditedMessageField(
                        field = field,
                        onEvent = component::onEvent
                    )
                }
                val parentMessage = field.parentMessage
                if (parentMessage != null) {
                    ParentMessageField(
                        parentMessage = parentMessage,
                        onEvent = component::onEvent
                    )
                }
                MessageField(
                    modifier = Modifier.fillMaxWidth(),
                    field = field,
                    followNewItems = followNewItems,
                    onEvent = component::onEvent
                )
            }
        }
    ) {
        Chat(
            modifier = Modifier
                .fillMaxWidth().innerShadow(
                    color = Color(color = 0xFFA1A1A1),
                ),
            items = state.messengerItems,
            loadingState = state.chatLoadingState,
            followNewItems = followNewItems,
            onEvent = component::onEvent
        )
    }
}

@Composable
private fun TopBar(person: PersonItem, userPresence: UserPresenceItem, onEvent: (MessengerEvent) -> Unit) {
    var showViewProfile by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(9.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(onClick = { onEvent(NavigateBackEvent) }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = CoreIcons.BackArrow,
                contentDescription = "Back",
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.clickable {
                showViewProfile = true
            }
        ) {
            ProfileAvatar(
                model = person.images.firstOrNull()?.raw(),
                modifier = Modifier.size(48.dp)
            )

            Column {
                Text(
                    text = person.name,
                    fontSize = 16.sp,
                )

                val textOnlineStatus = userPresence.text()
                Text(
                    text = textOnlineStatus,
                    fontSize = 10.sp
                )
            }
        }
    }

    if (showViewProfile) {
        val profileViewUIItem = person.toPersonView()
        ProfileViewBottomSheet(
            profileViewUIItem = profileViewUIItem,
            onDismissRequest = { showViewProfile = false },
            mode = ProfileViewMode.Match(
                onDeleteChat = {
                    onEvent(DeleteChatEvent)
                },
                onUnmatch = {
                    onEvent(DeleteMatchEvent)
                }
            )
        )
    }
}
