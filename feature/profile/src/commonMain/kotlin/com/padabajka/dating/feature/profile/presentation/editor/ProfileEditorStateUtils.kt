package com.padabajka.dating.feature.profile.presentation.editor

import com.padabajka.dating.core.domain.replaced
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUIItem
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorState
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedDetails

fun ProfileEditorState.changeAchievementMain(achievement: Achievement?): ProfileEditorState {
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

fun ProfileEditorState.makeAchievementVisible(achievement: Achievement): ProfileEditorState {
    val visibleAchievement = achievement.copy(hidden = false)
    return this.copy(
        achievements = achievements.updatedValue { it.replaced(achievement, visibleAchievement) }
    )
}

fun ProfileEditorState.hideAchievement(achievement: Achievement): ProfileEditorState {
    val hiddenAchievement = achievement.copy(hidden = true)
    return this.copy(
        mainAchievement = mainAchievement.updatedValue { it?.takeUnless { it == achievement } },
        achievements = achievements.updatedValue { it.replaced(achievement, hiddenAchievement) }
    )
}

fun ProfileEditorState.updateAboutMe(aboutMe: String): ProfileEditorState {
    return this.copy(aboutMe = this.aboutMe.updatedValue(aboutMe))
}

fun ProfileEditorState.updateName(name: String): ProfileEditorState {
    return this.copy(name = this.name.updatedValue(name))
}

fun ProfileEditorState.addImage(image: Image): ProfileEditorState {
    return this.copy(images = this.images.updatedValue { it.add(image) })
}

fun ProfileEditorState.deleteImage(image: Image): ProfileEditorState {
    return this.copy(images = this.images.updatedValue { it.remove(image) })
}

fun ProfileEditorState.updateLookingForData(data: LookingForData): ProfileEditorState {
    return this.copy(lookingFor = this.lookingFor.updatedValue(data))
}

fun ProfileEditorState.updateDetails(supportedDetails: SupportedDetails): ProfileEditorState {
    return this.copy(
        details = this.details.updatedValue { details ->
            details.copy(supportedDetails = supportedDetails)
        },
    )
}

fun ProfileEditorState.updateDetails(updated: SupportedDetails.() -> SupportedDetails): ProfileEditorState {
    return this.copy(
        details = this.details.updatedValue { details ->
            details.copy(supportedDetails = details.supportedDetails.updated())
        },
    )
}

fun ProfileEditorState.updateDetailCity(
    updated: DetailUIItem.AssetFromDb.() -> DetailUIItem.AssetFromDb
): ProfileEditorState {
    return updateDetails {
        copy(city = city.updated())
    }
}
