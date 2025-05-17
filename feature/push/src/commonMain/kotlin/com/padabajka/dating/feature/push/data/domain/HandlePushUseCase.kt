package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.feature.push.data.domain.model.DataPush
import com.padabajka.dating.feature.push.data.domain.model.PlatformMessagePush

class HandlePushUseCase(
    private val dataPushParser: DataPushParser,
    private val handleNewMatchUseCase: HandleNewMatchUseCase
) {
    suspend operator fun invoke(rawPush: PlatformMessagePush) {
        val dataPush = dataPushParser.parse(rawPush) ?: return
        when (dataPush) {
            is DataPush.NewMatch -> handleNewMatchUseCase(dataPush)
            is DataPush.NewMessage -> TODO()
        }
    }
}
