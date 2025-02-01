package com.fp.padabajka.feature.profile.data.network

import com.fp.padabajka.core.data.network.model.PersonResponse

interface ProfileApi {
    suspend fun get(): PersonResponse
    suspend fun patch(
        firstName: String?,
        lastName: String?,
        birthday: String?,
        aboutMe: String?
    )

    companion object {
        const val PATH_FOR_GET = "profile"
        const val PATH_FOR_PATCH = "update_profile"
    }
}
