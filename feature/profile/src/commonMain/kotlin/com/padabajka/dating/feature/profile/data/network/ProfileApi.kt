package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.PersonResponse
import kotlin.jvm.JvmInline

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
     * PATCH /profile?...
     */
    suspend fun patch(params: PatchParams)

    /**
     * POST /profile
     */
    suspend fun create(profile: ProfileRequest)

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
        const val PROFILE_PATH = "profile"
    }
}
