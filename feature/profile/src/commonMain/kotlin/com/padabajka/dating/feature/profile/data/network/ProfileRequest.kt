package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.DetailDto
import com.padabajka.dating.core.data.network.model.ImageDto
import com.padabajka.dating.core.data.network.model.LifestyleDto
import com.padabajka.dating.core.data.network.model.LookingForDataDto
import com.padabajka.dating.core.data.network.model.toDto
import com.padabajka.dating.core.data.network.model.toImageDto
import com.padabajka.dating.core.data.network.model.toLookingForDataDto
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequest(
    val name: String,
    val birthday: String,
    val gender: String,
    val images: List<ImageDto>,
    val aboutMe: String,
    val lookingFor: LookingForDataDto,
    val details: List<DetailDto>,
    val lifestyles: List<LifestyleDto>,
)

fun Profile.toRequest(gender: Gender): ProfileRequest {
    return ProfileRequest(
        name = name,
        birthday = birthday.toString(),
        images = images.filterIsInstance<Image.Url>().map { it.toImageDto() },
        aboutMe = aboutMe,
        details = details.map { it.toDto() },
        lifestyles = lifestyles.map { it.toDto() },
        gender = gender.raw,
        lookingFor = lookingFor.toLookingForDataDto(),
    )
}
