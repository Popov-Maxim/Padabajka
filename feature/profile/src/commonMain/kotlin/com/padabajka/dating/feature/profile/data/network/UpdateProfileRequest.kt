package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.DetailDto
import com.padabajka.dating.core.data.network.model.ImageDto
import com.padabajka.dating.core.data.network.model.LanguagesAssetDto
import com.padabajka.dating.core.data.network.model.LifestyleDto
import com.padabajka.dating.core.data.network.model.LookingForDataDto
import com.padabajka.dating.core.data.network.model.TextDto
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val birthday: String? = null,
    val aboutMe: String? = null,
    val images: List<ImageDto>? = null,
    val lookingFor: LookingForDataDto? = null,
    val details: List<DetailDto>? = null,
    val lifestyles: List<LifestyleDto>? = null,
    val languagesAsset: LanguagesAssetDto? = null,
    val interests: List<TextDto>? = null,
)
