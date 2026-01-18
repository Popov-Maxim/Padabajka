package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.PersonResponse
import kotlin.jvm.JvmInline

interface ProfileApi {
    suspend fun get(): PersonResponse?
    suspend fun get(userId: String): PersonResponse
    suspend fun patch(params: PatchParams)
    suspend fun create(profile: ProfileDto)

    @JvmInline
    value class PatchParams(val raw: Map<Key, String>) {
        enum class Key(val raw: String) {
            Name("name"),
            Birthday("birthday"),
            AboutMe("about_me"),
            Images("images"),
            LookingFor("looking_for"),
            Details("details"),
            Lifestyles("lifestyles"),
            LanguagesAsset("languages_asset"),
            Interests("interests"),
        }
    }

    companion object {
        const val PATH_FOR_GET = "profile"
        const val PATH_FOR_PATCH = "update_profile"
        const val PATH_FOR_CREATE_API = "create_profile"
    }
}
