package com.padabajka.dating.feature.swiper.data.reaction.network

import com.padabajka.dating.core.data.uuid
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.swiper.PersonReaction
import com.padabajka.dating.feature.push.notification.NotificationService
import com.padabajka.dating.feature.push.notification.model.NotificationChannel
import com.padabajka.dating.feature.swiper.presentation.screen.System

class FakeReactionApi(
    private val matchRepository: MatchRepository,
    private val personRepository: PersonRepository,
    private val notificationService: NotificationService
) : ReactionApi {
    override suspend fun postReactions(reactions: Set<PersonReaction>) {
        reactions.onEach {
            if (it is PersonReaction.SuperLike || it is PersonReaction.Like) {
                val person = personRepository.getPerson(it.id)
                matchRepository.saveMatch(
                    Match(
                        id = Match.Id(person.id.raw),
                        person = person,
                        chatId = ChatId(uuid()),
                        creationTime = System.now()
                    )
                )
                notificationService.showNotification(
                    (Int.MIN_VALUE..Int.MAX_VALUE).random(),
                    "New match",
                    "Match with ${person.profile.name}",
                    NotificationChannel.Match,
                )
            }
        }
    }
}
