package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.DetailDto
import com.padabajka.dating.core.data.network.model.ImageDto
import com.padabajka.dating.core.data.network.model.toDto
import com.padabajka.dating.core.data.network.model.toImageDto
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val name: String,
    val birthday: String,
    val images: List<ImageDto>,
    val aboutMe: String,
    val gender: String,
    val details: List<DetailDto>,
)

fun Profile.toDto(gender: Gender): ProfileDto {
    return ProfileDto(
        name = name,
        birthday = birthday.toString(),
        images = images.filterIsInstance<Image.Url>().map { it.toImageDto() },
        aboutMe = aboutMe,
        details = details.map { it.toDto() },
        gender = gender.raw
    )
}
