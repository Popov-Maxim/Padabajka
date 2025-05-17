package com.padabajka.dating.feature.swiper.data.reaction.network

import com.padabajka.dating.core.data.uuid
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.PersonRepository
import com.padabajka.dating.core.repository.api.model.match.Match
import com.padabajka.dating.core.repository.api.model.match.RawMatch
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.push.notification.NotificationService
import com.padabajka.dating.feature.push.notification.model.NotificationChannel
import com.padabajka.dating.feature.swiper.presentation.screen.System

class FakeReactionApi(
    private val matchRepository: MatchRepository,
    private val personRepository: PersonRepository,
    private val notificationService: NotificationService
) : ReactionApi {
    override suspend fun postReactions(reactions: Set<ReactionDto>) {
        reactions.onEach {
            if (it.reaction == ReactionType.SuperLike || it.reaction == ReactionType.Like) {
                val personId = it.reactedPersonId
                matchRepository.saveMatch(
                    RawMatch(
                        id = Match.Id(personId.raw),
                        personId = personId,
                        chatId = ChatId(uuid()),
                        creationTime = System.now()
                    )
                )
                val person = personRepository.getPerson(personId)
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
