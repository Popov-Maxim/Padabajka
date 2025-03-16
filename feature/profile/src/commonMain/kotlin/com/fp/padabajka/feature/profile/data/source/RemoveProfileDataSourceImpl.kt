package com.fp.padabajka.feature.profile.data.source

import com.fp.padabajka.core.data.network.model.ImageDto
import com.fp.padabajka.core.data.network.model.toImageDto
import com.fp.padabajka.core.data.network.model.toProfile
import com.fp.padabajka.core.domain.mapOfNotNull
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.profile.data.network.ProfileApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RemoveProfileDataSourceImpl(
    private val profileApi: ProfileApi
) : RemoveProfileDataSource {
    override suspend fun getProfile(): Profile {
        return profileApi.get().toProfile()
    }

    override suspend fun replace(current: Profile?, newProfile: Profile) {
        val firstName = fieldForUpdate(current, newProfile, Profile::firstName)
        val lastName = fieldForUpdate(current, newProfile, Profile::lastName)
        val birthday = fieldForUpdate(current, newProfile, Profile::birthday)?.toString()
        val aboutMe = fieldForUpdate(current, newProfile, Profile::aboutMe)
        // TODO(Image): need use only Image.Url in this code?
        val images = fieldForUpdate(current, newProfile, Profile::images)
            ?.filterIsInstance<Image.Url>()
            ?.map { it.toImageDto() }
        val parameters = mapOfNotNull(
            ProfileApi.PatchParams.Key.FirstName to firstName,
            ProfileApi.PatchParams.Key.LastName to lastName,
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
