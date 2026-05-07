package com.padabajka.dating.feature.profile.presentation.editor

import com.padabajka.dating.core.domain.replaced
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUIItem
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguageAssetsField
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesAssetType
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesAssetsFields
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorState
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileFields
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedDetails
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedLifestyles
import com.padabajka.dating.feature.profile.presentation.editor.model.updated
import com.padabajka.dating.feature.profile.presentation.model.AssetsFromDb
import kotlinx.collections.immutable.PersistentList

fun ProfileEditorState.updateFields(profile: Profile?, update: (ProfileFields) -> ProfileFields): ProfileEditorState {
    val fields = update(fields)
    val new = profile?.updated(fields)
    return copy(fields = fields, fieldsChanged = new != profile)
}

fun ProfileFields.changeAchievementMain(achievement: Achievement?): ProfileFields {
    return if (achievement == null) {
        this.copy(mainAchievement = this.mainAchievement.updatedValue(null))
    } else {
        val mainAchievement = achievement.copy(hidden = false)
        this.copy(
            mainAchievement = this.mainAchievement.updatedValue(mainAchievement),
            achievements = this.achievements.updatedValue {
                it.replaced(achievement, mainAchievement)
            }
        )
    }
}

fun ProfileFields.makeAchievementVisible(achievement: Achievement): ProfileFields {
    val visibleAchievement = achievement.copy(hidden = false)
    return this.copy(
        achievements = achievements.updatedValue { it.replaced(achievement, visibleAchievement) }
    )
}

fun ProfileFields.hideAchievement(achievement: Achievement): ProfileFields {
    val hiddenAchievement = achievement.copy(hidden = true)
    return this.copy(
        mainAchievement = mainAchievement.updatedValue { it?.takeUnless { it == achievement } },
        achievements = achievements.updatedValue { it.replaced(achievement, hiddenAchievement) }
    )
}

fun ProfileFields.updateAboutMe(aboutMe: String): ProfileFields {
    return this.copy(aboutMe = this.aboutMe.updatedValue(aboutMe))
}

fun ProfileFields.updateName(name: String): ProfileFields {
    return this.copy(name = this.name.updatedValue(name))
}

fun ProfileFields.addImage(image: Image, index: Int): ProfileFields {
    return this.copy(
        images = this.images.updatedValue {
            if (index < it.size) {
                it.add(index, image)
            } else {
                it.add(image)
            }
        }
    )
}

fun ProfileFields.deleteImage(image: Image): ProfileFields {
    return this.copy(images = this.images.updatedValue { it.remove(image) })
}

fun ProfileFields.updateLookingForData(data: LookingForData): ProfileFields {
    return this.copy(lookingFor = this.lookingFor.updatedValue(data))
}

fun ProfileFields.updateDetails(supportedDetails: SupportedDetails): ProfileFields {
    return this.copy(
        details = this.details.updatedValue { details ->
            details.copy(supportedDetails = supportedDetails)
        },
    )
}

fun ProfileFields.updateLifestyle(lifestyle: SupportedLifestyles): ProfileFields {
    return this.copy(
        lifeStyle = this.lifeStyle.updatedValue { details ->
            details.copy(supportedLifestyles = lifestyle)
        },
    )
}

fun ProfileFields.updateDetails(updated: SupportedDetails.() -> SupportedDetails): ProfileFields {
    return this.copy(
        details = this.details.updatedValue { details ->
            details.copy(supportedDetails = details.supportedDetails.updated())
        },
    )
}

fun ProfileFields.updateDetailCity(
    updated: DetailUIItem.AssetFromDb.() -> DetailUIItem.AssetFromDb
): ProfileFields {
    return updateDetails {
        copy(city = city.updated())
    }
}

fun ProfileFields.updateNativeLang(
    updated: LanguageAssetsField.() -> LanguageAssetsField
): ProfileFields {
    return updateLanguages {
        copy(nativeLanguages = nativeLanguages.updated())
    }
}

fun ProfileFields.updateKnownLang(
    updated: LanguageAssetsField.() -> LanguageAssetsField
): ProfileFields {
    return updateLanguages {
        copy(knownLanguages = knownLanguages.updated())
    }
}

fun ProfileFields.updateLearningLang(
    updated: LanguageAssetsField.() -> LanguageAssetsField
): ProfileFields {
    return updateLanguages {
        copy(learningLanguages = learningLanguages.updated())
    }
}

fun ProfileFields.updateLanguages(
    updated: LanguagesAssetsFields.() -> LanguagesAssetsFields
): ProfileFields {
    return copy(language = language.updatedValue(updated))
}

fun ProfileFields.changeLanguage(
    value: LanguageAssetsField,
    type: LanguagesAssetType
): ProfileFields {
    return when (type) {
        LanguagesAssetType.Native -> updateNativeLang { value }
        LanguagesAssetType.Known -> updateKnownLang { value }
        LanguagesAssetType.Learning -> updateLearningLang { value }
    }
}

fun ProfileFields.updateLanguage(
    updated: LanguageAssetsField.() -> LanguageAssetsField,
    type: LanguagesAssetType
): ProfileFields {
    return when (type) {
        LanguagesAssetType.Native -> updateNativeLang(updated)
        LanguagesAssetType.Known -> updateKnownLang(updated)
        LanguagesAssetType.Learning -> updateLearningLang(updated)
    }
}

fun ProfileFields.updateInterests(
    updated: AssetsFromDb.() -> AssetsFromDb
): ProfileFields {
    return this.copy(
        interests = this.interests.updatedValue { it.updated() }
    )
}

fun ProfileFields.changeInterests(
    value: PersistentList<Text>,
): ProfileFields {
    return updateInterests {
        copy(value = value)
    }
}
