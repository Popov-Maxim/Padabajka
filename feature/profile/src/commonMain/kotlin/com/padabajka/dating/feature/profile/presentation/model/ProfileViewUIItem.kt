package com.padabajka.dating.feature.profile.presentation.model

import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LanguagesAsset
import com.padabajka.dating.core.repository.api.model.profile.Lifestyle
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

data class ProfileViewUIItem(
    val name: String,
    val age: Age,
    val images: PersistentList<Image>,
    val aboutMe: String,
    val lookingFor: LookingForData,
    val details: PersistentList<Detail>,
    val lifestyle: PersistentList<Lifestyle>,
    val languages: LanguagesAsset
)

fun Profile.toPersonView(): ProfileViewUIItem {
    return ProfileViewUIItem(
        name = name,
        age = age,
        images = images.toPersistentList(),
        aboutMe = aboutMe,
        lookingFor = lookingFor,
        details = details.toPersistentList(),
        lifestyle = lifestyles.toPersistentList(),
        languages = languagesAsset
    )
}
