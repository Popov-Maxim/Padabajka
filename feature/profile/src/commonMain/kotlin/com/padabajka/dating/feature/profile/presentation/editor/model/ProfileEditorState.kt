package com.padabajka.dating.feature.profile.presentation.editor.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEvent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.profile.age
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate

@Immutable
data class ProfileEditorState(
    val name: ProfileField<String>,
    val birthday: ProfileField<LocalDate>,
    val images: ProfileField<PersistentList<Image>>,
    val aboutMe: ProfileField<String>,
    val lookingFor: ProfileField<LookingForData>,
    val details: ProfileField<DetailFields>,
    val lifeStyle: ProfileField<LifestyleFields>,
    val language: ProfileField<LanguagesAssetsFields>,
    val mainAchievement: ProfileField<Achievement?>,
    val achievements: ProfileField<PersistentList<Achievement>>,
    val saveState: SaveState = SaveState.Idle,
    val internalErrorStateEvent: StateEvent = consumed
) : State {

    @Stable
    val profileForPreview: ProfileViewUIItem = ProfileViewUIItem(
        name = name.value,
        age = birthday.value.age,
        images = images.value,
        aboutMe = aboutMe.value,
        lookingFor = lookingFor.value,
        details = details.value.allDetails,
        lifestyle = lifeStyle.value.toDomain().toPersistentList(),
        languages = language.value.toDomain()
    )

    sealed interface SaveState {
        object Idle : SaveState
        object Loading : SaveState
        object Error : SaveState
    }
}

data class ProfileField<T>(
    val value: T,
    val issues: Map<T, Issue> = emptyMap()
) {

    @Stable
    val currentIssue: Issue? = issues[value]

    fun updatedValue(value: T) = copy(value = value)
    fun updatedValue(action: (T) -> T) = copy(value = action(value))

    fun updatedIssues(action: (Map<T, Issue>) -> Map<T, Issue>) = copy(issues = action(issues))
}

sealed interface Issue {
    data class StringValue(
        val message: String
    ) : Issue

    data class Text(
        val message: StaticTextId
    ) : Issue

    @Composable
    fun toMessage(): String {
        return when (this) {
            is StringValue -> message
            is Text -> message.translate()
        }
    }
}

fun Profile.toEditorState(): ProfileEditorState {
    return ProfileEditorState(
        name = name.toField(),
        birthday = birthday.toField(),
        images = images.toPersistentList().toField(),
        aboutMe = aboutMe.toField(),
        lookingFor = lookingFor.toField(),
        details = details.toDetailFields().toField(),
        mainAchievement = mainAchievement.toField(),
        achievements = achievements.toPersistentList().toField(),
        lifeStyle = lifestyles.toLifestyleFields().toField(),
        language = languagesAsset.toLanguagesFields().toField()
    )
}

fun Profile.updated(state: ProfileEditorState) = copy(
    name = state.name.value,
    birthday = state.birthday.value,
    images = state.images.value,
    aboutMe = state.aboutMe.value,
    lookingFor = state.lookingFor.value,
    details = state.details.value.allDetails,
    lifestyles = state.lifeStyle.value.toDomain(),
    languagesAsset = state.language.value.toDomain(),
    mainAchievement = state.mainAchievement.value,
    achievements = state.achievements.value
)

private fun <T> T.toField(): ProfileField<T> {
    return ProfileField(this)
}
