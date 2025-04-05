package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.PersonResponse
import kotlin.jvm.JvmInline

interface ProfileApi {
    suspend fun get(): PersonResponse
    suspend fun patch(params: PatchParams)

    @JvmInline
    value class PatchParams(val raw: Map<Key, String>) {
        enum class Key(val raw: String) {
            Name("name"),
            Birthday("birthday"),
            AboutMe("about_me"),
            Images("images"),
        }
    }

    companion object {
        const val PATH_FOR_GET = "profile"
        const val PATH_FOR_PATCH = "update_profile"
    }
}
