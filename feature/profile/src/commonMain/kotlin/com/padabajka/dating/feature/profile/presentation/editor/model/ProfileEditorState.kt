package com.padabajka.dating.feature.profile.presentation.editor.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEvent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.profile.age
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate

@Immutable
data class ProfileEditorState(
    val name: ProfileField<String>,
    val birthday: ProfileField<LocalDate>,
    val images: ProfileField<PersistentList<Image>>,
    val aboutMe: ProfileField<String>,
    val lookingFor: ProfileField<LookingForData>,
    val details: ProfileField<PersistentList<Detail>>,
    val mainAchievement: ProfileField<Achievement?>,
    val achievements: ProfileField<PersistentList<Achievement>>,
    val internalErrorStateEvent: StateEvent = consumed
) : State {

    @Stable
    val profileForPreview: ProfileViewUIItem = ProfileViewUIItem(
        name = name.value,
        age = birthday.value.age,
        images = images.value,
        aboutMe = aboutMe.value,
        lookingFor = lookingFor.value,
        details = details.value,
        lifestyle = persistentListOf(),
    )

    fun updated(profile: Profile): ProfileEditorState = copy(
        name = name.copy(value = profile.name),
        birthday = birthday.copy(value = profile.birthday),
        images = images.copy(value = profile.images),
        aboutMe = aboutMe.copy(value = profile.aboutMe),
        lookingFor = lookingFor.copy(value = profile.lookingFor),
        details = details.copy(value = profile.details.toPersistentList()),
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
        name = name.toField(),
        birthday = birthday.toField(),
        images = images.toField(),
        aboutMe = aboutMe.toField(),
        lookingFor = lookingFor.toField(),
        details = details.toPersistentList().toField(),
        mainAchievement = mainAchievement.toField(),
        achievements = achievements.toField(),
    )
}

fun Profile.updated(state: ProfileEditorState) = copy(
    name = state.name.value,
    birthday = state.birthday.value,
    images = state.images.value,
    aboutMe = state.aboutMe.value,
    lookingFor = state.lookingFor.value,
    details = state.details.value,
    mainAchievement = state.mainAchievement.value,
    achievements = state.achievements.value
)

private fun <T> T.toField(): ProfileField<T> {
    return ProfileField(this)
}
