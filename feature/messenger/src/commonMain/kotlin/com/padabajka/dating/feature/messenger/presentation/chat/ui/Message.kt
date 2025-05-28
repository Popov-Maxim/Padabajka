package com.padabajka.dating.feature.messenger.presentation.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.hourMinutes
import com.padabajka.dating.core.presentation.ui.AnimatedPopup
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomViewConfiguration
import com.padabajka.dating.core.presentation.ui.ProfileAvatar
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.IconData
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessageGotReadEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.ReactToMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.SelectParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.IncomingMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.OutgoingMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.status
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.toParentMessageItem
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun Message(
    message: MessageItem,
    shape: Shape,
    onEvent: (MessengerEvent) -> Unit
): Unit = CustomViewConfiguration(
    doubleTapTimeoutMillis = 150
) {
    if (message is IncomingMessageItem) {
        SideEffect {
            if (message.hasBeenRead.not()) {
                onEvent(MessageGotReadEvent(message.id))
            }
        }
    }

    var enabledDropdown: Boolean by remember { mutableStateOf(false) }

    val textColor = when (message) {
        is IncomingMessageItem -> Color.Black
        is OutgoingMessageItem -> CoreColors.Chat.message.textColor
    }
    val parentMessageColor = getParentMessageColor(message)

    Box(
        modifier = Modifier.fillMaxWidth()
            .swipeToReply {
                onEvent(SelectParentMessageEvent(message.toParentMessageItem()))
            }.pointerInput(message.reactions) {
                detectTapGestures(
                    onTap = {
                        enabledDropdown = true
                    },
                    onDoubleTap = {
                        onEvent(likeEvent(message))
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .alignForMessage(message, this)
        ) {
            when (message.status()) {
                MessageStatus.Sending -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(16.dp),
                        strokeWidth = 2.dp
                    )
                }

                MessageStatus.FailedToSend -> {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Back",
                    )
                }

                MessageStatus.Sent,
                null -> Unit
            }
            Box(
                modifier = Modifier
                    .shadow(2.dp, shape)
                    .backgroundForMessage(message, shape)
            ) {
                val parentMessage = message.parentMessage
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max)
                        .widthIn(max = 300.dp)
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            top = if (parentMessage == null) 2.dp else 10.dp,
                            bottom = 2.dp,
                        ),
                ) {
                    if (parentMessage != null) {
                        CommonParentMessage(
                            parentMessage = parentMessage,
                            modifier = Modifier.fillMaxWidth().widthIn(min = 100.dp),
                            colors = parentMessageColor
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.height(IntrinsicSize.Max).padding(vertical = 4.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = message.content,
                                color = textColor,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f),
                            )

                            if (message.reactions.isNotEmpty()) {
                                Box(
                                    modifier = Modifier.background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(4.dp),
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(20.dp),
                                            painter = CoreIcons.Reaction.Like,
                                            tint = Color.Red,
                                            contentDescription = "reaction like"
                                        )
                                        message.reactions.onEach { reaction ->
                                            ProfileAvatar(
                                                model = reaction.author.profile.images.first()
                                                    .raw(),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = message.sentTime.hourMinutes,
                            color = textColor,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
        AnimatedPopup(
            visible = enabledDropdown,
            onDismissRequest = {
                enabledDropdown = false
            }
        ) {
            PopupMessageMenuContent(
                message = message,
                onEvent = onEvent,
                onDismissRequest = {
                    enabledDropdown = false
                }
            )
        }
    }
}

@Composable
private fun Modifier.backgroundForMessage(
    message: MessageItem,
    shape: Shape
): Modifier {
    val backgroundColor = when (message) {
        is IncomingMessageItem -> Color.White
        is OutgoingMessageItem -> CoreColors.Chat.message.mainColor
    }

    return this.background(backgroundColor, shape)
}

@Composable
private fun Modifier.alignForMessage(
    message: MessageItem,
    boxScope: BoxScope
): Modifier {
    val alignment = when (message) {
        is IncomingMessageItem -> Alignment.CenterStart
        is OutgoingMessageItem -> Alignment.CenterEnd
    }
    with(boxScope) {
        return this@alignForMessage.align(alignment)
    }
}

@Composable
fun Modifier.swipeToReply(
    threshold: Float = 200f,
    onReply: () -> Unit
): Modifier {
    var swipeOffset by remember { mutableStateOf(0f) }

    return this
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    if (abs(swipeOffset) >= threshold) {
                        onReply()
                    }
                    swipeOffset = 0f
                }
            ) { change, dragAmount ->
                val newOffset = min(0f, swipeOffset + dragAmount)
                swipeOffset = max(newOffset, -threshold)
                change.consume()
            }
        }
        .offset { IntOffset(swipeOffset.roundToInt(), 0) }
}

@Stable
private fun getParentMessageColor(message: MessageItem): ParentMessageColors {
    val parentMessageColor = ParentMessageColors.default().copy(
        textColor = when (message) {
            is IncomingMessageItem -> Color.Black
            is OutgoingMessageItem -> Color.White
        },
        authorColor = when (message) {
            is IncomingMessageItem -> CoreColors.Chat.secondary.mainColor
            is OutgoingMessageItem -> Color.White
        },
        messageHighlightColor = when (message) {
            is IncomingMessageItem -> CoreColors.Chat.secondary.mainColor
            is OutgoingMessageItem -> Color.White
        },
        backgroundBrush = when (message) {
            is IncomingMessageItem -> Brush.horizontalGradient(
                colors = listOf(
                    CoreColors.Chat.secondary.mainColor.copy(alpha = 0.15f),
                    CoreColors.Chat.secondary.mainColor.copy(alpha = 0.05f),
                )
            )

            is OutgoingMessageItem -> Brush.horizontalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.25f),
                    Color.Transparent,
                )
            )
        }
    )

    return parentMessageColor
}

@Composable
private fun PopupMessageMenuContent(
    message: MessageItem,
    onEvent: (MessengerEvent) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val popupButtonData = listOf(
        PopupButtonData(
            iconData = CoreIcons.Popup.Like.toData(),
            text = StaticTextId.UiId.MessagePopupLike,
            onClick = { onEvent(likeEvent(message)) }
        ),
        PopupButtonData(
            iconData = CoreIcons.Popup.Reply.toData(),
            text = StaticTextId.UiId.MessagePopupReply,
            onClick = { onEvent(SelectParentMessageEvent(message.toParentMessageItem())) }
        ),
        PopupButtonData(
            iconData = CoreIcons.Popup.Copy.toData(),
            text = StaticTextId.UiId.MessagePopupCopy,
            onClick = { }
        ),
        PopupButtonData(
            iconData = CoreIcons.Popup.Pin.toData(),
            text = StaticTextId.UiId.MessagePopupPin,
            onClick = { }
        ),
        PopupButtonData(
            iconData = CoreIcons.Popup.Edit.toData(),
            text = StaticTextId.UiId.MessagePopupEdit,
            onClick = { }
        ),
        PopupButtonData(
            iconData = CoreIcons.Popup.Trash.toData(),
            text = StaticTextId.UiId.MessagePopupDelete,
            onClick = { }
        )
    )

    val shape = RoundedCornerShape(10.dp)
    Column(
        Modifier.shadow(4.dp, shape).background(Color.White, shape)
            .width(IntrinsicSize.Max).widthIn(min = 200.dp)
    ) {
        popupButtonData.onEach { data ->
            Box(
                modifier = Modifier.fillMaxWidth().clickable {
                    data.onClick()
                    onDismissRequest()
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        iconData = data.iconData,
                        modifier = Modifier.size(24.dp),
                        contentDescription = null
                    )
                    Text(
                        text = data.text.translate(),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

private data class PopupButtonData(
    val iconData: IconData,
    val text: StaticTextId,
    val onClick: () -> Unit
)

private fun likeEvent(
    message: MessageItem
): ReactToMessageEvent {
    return if (message.reactions.isEmpty()) {
        ReactToMessageEvent(
            message.id,
            reaction = MessageReaction.Value.Like
        )
    } else {
        ReactToMessageEvent(message.id, reaction = null)
    }
}
