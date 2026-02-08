package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.MatchDataPush
import com.padabajka.dating.feature.match.data.source.local.LocalMatchDataSource

class HandleUpdateMatchUseCase(
    private val localMatchDataSource: LocalMatchDataSource,
//    private val notificationService: NotificationService,
) {
    suspend operator fun invoke(dataPush: MatchDataPush.UpdateMatch) {
        localMatchDataSource.update(dataPush.id) { match ->
            match.copy(
                chatId = dataPush.newChatId.raw
            )
        }
//        notificationService.showNotification(
//            (Int.MIN_VALUE..Int.MAX_VALUE).random(),
//            "New match",
//            "Match with ${dataPush.personName}",
//            NotificationChannel.Match,
//        )
    }
}
