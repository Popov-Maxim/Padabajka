package com.padabajka.dating.feature.match.data.source.remote

import com.padabajka.dating.feature.match.data.model.MatchDto

interface MatchesApi {
    suspend fun getMatches(): List<MatchDto>

    companion object {
        const val PATH = "matches"
    }
}
