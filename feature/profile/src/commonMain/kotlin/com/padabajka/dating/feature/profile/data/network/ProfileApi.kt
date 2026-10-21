package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.PersonResponse

interface ProfileApi {

    /**
     * GET /profile
     */
    suspend fun get(): PersonResponse?

    /**
     * GET /profile/{userId}
     */
    suspend fun get(userId: String): PersonResponse?

    /**
     * PATCH /profile with a JSON body. Null fields leave existing values unchanged.
     */
    suspend fun patch(request: UpdateProfileRequest)

    /**
     * POST /profile
     */
    suspend fun create(profile: ProfileRequest)

    companion object {
        const val PROFILE_PATH = "profile"
    }
}
