package com.padabajka.dating.feature.messenger.presentation.model

import androidx.compose.runtime.Stable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LanguagesAsset
import com.padabajka.dating.core.repository.api.model.profile.Lifestyle
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.swiper.Person
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessageItem
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

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
    val lastMessage: MessageItem,
    val unreadMessagesCount: Int
)

data class MatchItem(
    val id: Match.Id,
    val person: PersonItem,
    val creationTime: Long
)

@Serializable
data class PersonItem(
    val name: String,
    val age: Age,
    val images: PersistentList<Image>,
    val aboutMe: String,
    val lookingFor: LookingForData,
    val details: PersistentList<Detail>,
    val lifestyles: PersistentList<Lifestyle>,
    val languages: LanguagesAsset
)

fun Person.toPersonItem() = PersonItem(
    name = profile.name,
    age = profile.age,
    images = profile.images.toPersistentList(),
    aboutMe = profile.aboutMe,
    lookingFor = profile.lookingFor,
    details = profile.details.toPersistentList(),
    lifestyles = profile.lifestyles.toPersistentList(),
    languages = profile.languagesAsset
)

@Stable
fun PersonItem.toPersonView(): ProfileViewUIItem {
    return ProfileViewUIItem(
        name = name,
        age = age,
        images = images,
        aboutMe = aboutMe,
        lookingFor = lookingFor,
        details = details,
        lifestyle = lifestyles,
        languages = languages
    )
}
