package com.fp.padabajka.feature.profile.presentation.editor

import com.fp.padabajka.core.domain.replaced
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.feature.profile.presentation.editor.model.ProfileEditorState

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

fun ProfileEditorState.updateLastName(lastName: String): ProfileEditorState {
    return this.copy(lastName = this.lastName.updatedValue(lastName))
}

fun ProfileEditorState.updateFirstName(firstName: String): ProfileEditorState {
    return this.copy(firstName = this.firstName.updatedValue(firstName))
}
