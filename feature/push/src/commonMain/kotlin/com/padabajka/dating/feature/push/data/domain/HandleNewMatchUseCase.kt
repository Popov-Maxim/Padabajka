package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.model.match.RawMatch
import com.padabajka.dating.core.repository.api.model.push.DataPush
import com.padabajka.dating.feature.push.notification.NotificationService
import com.padabajka.dating.feature.push.notification.model.NotificationChannel

class HandleNewMatchUseCase(
    private val matchRepository: MatchRepository,
    private val notificationService: NotificationService
) {
    suspend operator fun invoke(dataPush: DataPush.NewMatch) {
        val rawMatch = dataPush.toRawMatch()
        matchRepository.saveMatch(rawMatch)
        notificationService.showNotification(
            (Int.MIN_VALUE..Int.MAX_VALUE).random(),
            "New match",
            "Match with ${dataPush.personName}",
            NotificationChannel.Match,
        )
    }

    private fun DataPush.NewMatch.toRawMatch(): RawMatch {
        return RawMatch(
            id = id,
            personId = personId,
            chatId = chatId,
            creationTime = creationTime
        )
    }
}
