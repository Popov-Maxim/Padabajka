package com.padabajka.dating.feature.profile.data.source

import com.padabajka.dating.core.data.network.model.toDto
import com.padabajka.dating.core.data.network.model.toImageDto
import com.padabajka.dating.core.data.network.model.toLookingForDataDto
import com.padabajka.dating.core.data.network.model.toProfile
import com.padabajka.dating.core.data.network.model.toTextDto
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.feature.profile.data.network.AccountApi
import com.padabajka.dating.feature.profile.data.network.ProfileApi
import com.padabajka.dating.feature.profile.data.network.UpdateProfileRequest
import com.padabajka.dating.feature.profile.data.network.toRequest

class RemoveProfileDataSourceImpl(
    private val profileApi: ProfileApi,
    private val accountApi: AccountApi
) : RemoveProfileDataSource {
    override suspend fun getProfile(): Profile? {
        return profileApi.get()?.toProfile()
    }

    override suspend fun getProfile(userId: String): Profile? {
        return profileApi.get(userId)?.toProfile()
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
        val lifestyles = fieldForUpdate(current, newProfile, Profile::lifestyles)
            ?.map { it.toDto() }
        val languages = fieldForUpdate(current, newProfile, Profile::languagesAsset)?.toDto()
        val interests = fieldForUpdate(current, newProfile, Profile::interests)
            ?.map { it.toTextDto() }
        val request = UpdateProfileRequest(
            name = name,
            birthday = birthday,
            aboutMe = aboutMe,
            images = images,
            lookingFor = lookingFor,
            details = details,
            lifestyles = lifestyles,
            languagesAsset = languages,
            interests = interests,
        )
        profileApi.patch(request)
    }

    override suspend fun create(profile: Profile, gender: Gender) {
        profileApi.create(profile.toRequest(gender))
    }

    override suspend fun setFreeze(freeze: Boolean) {
        if (freeze) {
            accountApi.freeze()
        } else {
            accountApi.unfreeze()
        }
    }

    private fun <T> fieldForUpdate(current: Profile?, newProfile: Profile, field: Profile.() -> T): T? {
        return newProfile.field().takeIf { it != current?.field() }
    }
}
