package com.padabajka.dating.feature.match.data.source.remote

import com.padabajka.dating.feature.match.data.model.MatchDto

interface MatchesApi {

    /**
     * GET /matches
     */
    suspend fun getMatches(): List<MatchDto>

    /**
     * DELETE /match/{matchId}
     */
    suspend fun delete(matchId: String)

    companion object {
        const val MATCHES_PATH = "matches"
        const val MATCH_PATH = "match"
    }
}
