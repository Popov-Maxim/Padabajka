package com.fp.padabajka.feature.profile.data.network

import com.fp.padabajka.core.data.network.model.PersonResponse
import kotlin.jvm.JvmInline

interface ProfileApi {
    suspend fun get(): PersonResponse
    suspend fun patch(params: PatchParams)

    @JvmInline
    value class PatchParams(val raw: Map<Key, String>) {
        enum class Key(val raw: String) {
            FirstName("first_name"),
            LastName("last_name"),
            Birthday("birthday"),
            AboutMe("about_me"),
        }
    }

    companion object {
        const val PATH_FOR_GET = "profile"
        const val PATH_FOR_PATCH = "update_profile"
    }
}
