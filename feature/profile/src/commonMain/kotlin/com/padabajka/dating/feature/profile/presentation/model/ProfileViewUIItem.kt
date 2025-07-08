package com.padabajka.dating.feature.profile.presentation.model

import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Lifestyle
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import kotlinx.collections.immutable.PersistentList

data class ProfileViewUIItem(
    val name: String,
    val age: Age,
    val images: PersistentList<Image>,
    val aboutMe: String,
    val lookingFor: LookingForData,
    val details: PersistentList<Detail>,
    val lifestyle: PersistentList<Lifestyle>,
)
