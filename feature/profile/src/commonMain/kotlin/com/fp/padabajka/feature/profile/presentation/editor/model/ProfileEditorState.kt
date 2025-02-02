package com.fp.padabajka.feature.profile.presentation.editor.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.presentation.event.StateEvent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.core.repository.api.model.profile.Detail
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.datetime.LocalDate

@Immutable
data class ProfileEditorState(
    val firstName: ProfileItem<String>,
    val lastName: ProfileItem<String>,
    val birthday: ProfileItem<LocalDate>,
    val images: ProfileItem<List<Image>>,
    val aboutMe: ProfileItem<String>,
    val details: ProfileItem<List<Detail>>,
    val mainAchievement: ProfileItem<Achievement?>,
    val achievements: ProfileItem<List<Achievement>>,
    val internalErrorStateEvent: StateEvent = consumed
) : State {

    fun updated(profile: Profile): ProfileEditorState = copy(
        firstName = firstName.copy(value = profile.firstName),
        lastName = lastName.copy(value = profile.lastName),
        birthday = birthday.copy(value = profile.birthday),
        images = images.copy(value = profile.images),
        aboutMe = aboutMe.copy(value = profile.aboutMe),
        details = details.copy(value = profile.details),
        mainAchievement = mainAchievement.copy(value = profile.mainAchievement),
        achievements = achievements.copy(value = profile.achievements)
    )
}

data class ProfileItem<T>(
    val value: T,
    val issues: Map<T, Issue> = emptyMap()
)

data class Issue(
    val message: String
)
