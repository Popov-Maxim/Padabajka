package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.MatchDataPush
import com.padabajka.dating.core.repository.api.MatchRepository
import com.padabajka.dating.core.repository.api.model.match.RawMatch

class HandleNewMatchUseCase(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(dataPush: MatchDataPush.NewMatch) {
        val rawMatch = dataPush.toRawMatch()
        matchRepository.saveMatch(rawMatch)
    }

    private fun MatchDataPush.NewMatch.toRawMatch(): RawMatch {
        return RawMatch(
            id = id,
            personId = personId,
            chatId = chatId,
            creationTime = creationTime
        )
    }
}
