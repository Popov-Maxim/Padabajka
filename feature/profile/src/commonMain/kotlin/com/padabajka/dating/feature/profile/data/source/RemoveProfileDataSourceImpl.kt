package com.padabajka.dating.feature.profile.data.source

import com.padabajka.dating.core.data.network.model.ImageDto
import com.padabajka.dating.core.data.network.model.toImageDto
import com.padabajka.dating.core.data.network.model.toProfile
import com.padabajka.dating.core.domain.mapOfNotNull
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.profile.data.network.ProfileApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RemoveProfileDataSourceImpl(
    private val profileApi: ProfileApi
) : RemoveProfileDataSource {
    override suspend fun getProfile(): Profile {
        return profileApi.get().toProfile()
    }

    override suspend fun replace(current: Profile?, newProfile: Profile) {
        val name = fieldForUpdate(current, newProfile, Profile::name)
        val birthday = fieldForUpdate(current, newProfile, Profile::birthday)?.toString()
        val aboutMe = fieldForUpdate(current, newProfile, Profile::aboutMe)
        // TODO(Image): need use only Image.Url in this code?
        val images = fieldForUpdate(current, newProfile, Profile::images)
            ?.filterIsInstance<Image.Url>()
            ?.map { it.toImageDto() }
        val parameters = mapOfNotNull(
            ProfileApi.PatchParams.Key.Name to name,
            ProfileApi.PatchParams.Key.Birthday to birthday,
            ProfileApi.PatchParams.Key.AboutMe to aboutMe,
            ProfileApi.PatchParams.Key.Images to images?.serializeToString()
        )
        profileApi.patch(ProfileApi.PatchParams(parameters))
    }

    private fun <T> fieldForUpdate(current: Profile?, newProfile: Profile, field: Profile.() -> T): T? {
        return newProfile.field().takeIf { it != current?.field() }
    }

    private fun List<ImageDto>.serializeToString(): String {
        return Json.encodeToString(this)
    }
}
