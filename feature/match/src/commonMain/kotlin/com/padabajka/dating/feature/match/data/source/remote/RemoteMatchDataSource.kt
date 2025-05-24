package com.padabajka.dating.feature.match.data.source.remote

import com.padabajka.dating.feature.match.data.model.MatchDto

class RemoteMatchDataSource(
    private val matchesApi: MatchesApi
) {
    suspend fun loadMatches(): List<MatchDto> {
        return matchesApi.getMatches()
    }
}
