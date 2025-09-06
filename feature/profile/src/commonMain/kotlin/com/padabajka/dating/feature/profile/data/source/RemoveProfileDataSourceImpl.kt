package com.padabajka.dating.feature.profile.data.source

import com.padabajka.dating.core.data.network.model.toDto
import com.padabajka.dating.core.data.network.model.toImageDto
import com.padabajka.dating.core.data.network.model.toLookingForDataDto
import com.padabajka.dating.core.data.network.model.toProfile
import com.padabajka.dating.core.domain.mapOfNotNull
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.profile.data.network.ProfileApi
import com.padabajka.dating.feature.profile.data.network.toDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RemoveProfileDataSourceImpl(
    private val profileApi: ProfileApi
) : RemoveProfileDataSource {
    override suspend fun getProfile(): Profile? {
        return profileApi.get()?.toProfile()
    }

    override suspend fun getProfile(userId: String): Profile {
        return profileApi.get(userId).toProfile()
    }

    override suspend fun replace(current: Profile?, newProfile: Profile) {
        val name = fieldForUpdate(current, newProfile, Profile::name)
        val birthday = fieldForUpdate(current, newProfile, Profile::birthday)?.toString()
        val aboutMe = fieldForUpdate(current, newProfile, Profile::aboutMe)
        // TODO(Image): need use only Image.Url in this code?
        val images = fieldForUpdate(current, newProfile, Profile::images)
            ?.filterIsInstance<Image.Url>()
            ?.map { it.toImageDto() }
        val lookingFor = fieldForUpdate(current, newProfile, Profile::lookingFor)
            ?.toLookingForDataDto()
        val details = fieldForUpdate(current, newProfile, Profile::details)
            ?.map { it.toDto() }
        val parameters = mapOfNotNull(
            ProfileApi.PatchParams.Key.Name to name,
            ProfileApi.PatchParams.Key.Birthday to birthday,
            ProfileApi.PatchParams.Key.AboutMe to aboutMe,
            ProfileApi.PatchParams.Key.Images to images?.serializeToString(),
            ProfileApi.PatchParams.Key.LookingFor to lookingFor?.serializeToString(),
            ProfileApi.PatchParams.Key.Details to details?.serializeToString()
        )
        profileApi.patch(ProfileApi.PatchParams(parameters))
    }

    override suspend fun create(profile: Profile, gender: Gender) {
        profileApi.create(profile.toDto(gender))
    }

    private fun <T> fieldForUpdate(current: Profile?, newProfile: Profile, field: Profile.() -> T): T? {
        return newProfile.field().takeIf { it != current?.field() }
    }

    private inline fun <reified T> T.serializeToString(): String {
        return Json.encodeToString(this)
    }
}
