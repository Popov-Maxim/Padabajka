package com.padabajka.dating.feature.messenger.presentation.model

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessageItem
import kotlinx.collections.immutable.PersistentList

data class MessengerState(
    val matchesWithoutChat: PersistentList<EmptyChatItem>,
    val chats: PersistentList<ChatItem>
) : State

data class EmptyChatItem(
    val chatId: ChatId,
    val match: MatchItem,
)

data class ChatItem(
    val chatId: ChatId,
    val match: MatchItem,
    val lastMessage: MessageItem
)

data class MatchItem(
    val id: Match.Id,
    val person: PersonItem,
    val creationTime: Long
)

data class PersonItem(
    val id: PersonId,
    val name: String,
    val image: Image,
)

fun Person.toPersonItem() = PersonItem(
    id = id,
    name = profile.name,
    image = profile.images.first()
)
