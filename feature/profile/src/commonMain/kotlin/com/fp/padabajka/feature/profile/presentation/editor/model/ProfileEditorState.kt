package com.fp.padabajka.feature.profile.presentation.editor.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.presentation.event.StateEvent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.core.repository.api.model.profile.Detail
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.LocalDate

@Immutable
data class ProfileEditorState(
    val firstName: ProfileField<String>,
    val lastName: ProfileField<String>,
    val birthday: ProfileField<LocalDate>,
    val images: ProfileField<PersistentList<Image>>,
    val aboutMe: ProfileField<String>,
    val details: ProfileField<PersistentList<Detail>>,
    val mainAchievement: ProfileField<Achievement?>,
    val achievements: ProfileField<PersistentList<Achievement>>,
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

data class ProfileField<T>(
    val value: T,
    val issues: Map<T, Issue> = emptyMap()
) {
    fun updatedValue(value: T) = copy(value = value)
    fun updatedValue(action: (T) -> T) = copy(value = action(value))
}

data class Issue(
    val message: String
)

fun Profile.toEditorState(): ProfileEditorState {
    return ProfileEditorState(
        firstName = firstName.toField(),
        lastName = lastName.toField(),
        birthday = birthday.toField(),
        images = images.toField(),
        aboutMe = aboutMe.toField(),
        details = details.toField(),
        mainAchievement = mainAchievement.toField(),
        achievements = achievements.toField()
    )
}

fun Profile.updated(state: ProfileEditorState) = copy(
    firstName = state.firstName.value,
    lastName = state.lastName.value,
    birthday = state.birthday.value,
//    images = state.images.value,
    aboutMe = state.aboutMe.value,
    details = state.details.value,
    mainAchievement = state.mainAchievement.value,
    achievements = state.achievements.value
)

private fun <T> T.toField(): ProfileField<T> {
    return ProfileField(this)
}
