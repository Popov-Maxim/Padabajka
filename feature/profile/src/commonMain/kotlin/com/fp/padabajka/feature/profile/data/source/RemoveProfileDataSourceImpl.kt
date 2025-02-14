package com.fp.padabajka.feature.profile.data.source

import com.fp.padabajka.core.data.network.model.toProfile
import com.fp.padabajka.core.domain.mapOfNotNull
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.feature.profile.data.network.ProfileApi

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
        val parameters = mapOfNotNull(
            ProfileApi.PatchParams.Key.FirstName to firstName,
            ProfileApi.PatchParams.Key.LastName to lastName,
            ProfileApi.PatchParams.Key.Birthday to birthday,
            ProfileApi.PatchParams.Key.AboutMe to aboutMe,
        )
        profileApi.patch(ProfileApi.PatchParams(parameters))
    }

    private fun <T> fieldForUpdate(current: Profile?, newProfile: Profile, field: Profile.() -> T): T? {
        return newProfile.field().takeIf { newProfile.field() != current?.field() }
    }
}
