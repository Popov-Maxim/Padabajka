package com.padabajka.dating.feature.match.data.source.remote

import com.padabajka.dating.feature.match.data.model.MatchDto

interface MatchesApi {

    /**
     * GET /matches
     */
    suspend fun getMatches(): List<MatchDto>

    /**
     * DELETE /match_delete/{matchId}
     */
    suspend fun delete(matchId: String)

    companion object {
        const val PATH = "matches"
        const val PATH_DELETE = "match_delete"
    }
}
