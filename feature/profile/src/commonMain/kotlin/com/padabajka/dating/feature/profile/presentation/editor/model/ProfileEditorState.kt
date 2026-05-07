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
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.core.repository.api.model.profile.age
import com.padabajka.dating.feature.profile.presentation.model.AssetsFromDb
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate

@Immutable
data class ProfileEditorState(
    val fields: ProfileFields,
    val fieldsChanged: Boolean,
    val saveState: SaveState = SaveState.Idle,
    val internalErrorStateEvent: StateEvent = consumed
) : State {

    @Stable
    val profileForPreview: ProfileViewUIItem = ProfileViewUIItem(
        name = fields.name.value,
        age = fields.birthday.value.age,
        images = fields.images.value,
        aboutMe = fields.aboutMe.value,
        lookingFor = fields.lookingFor.value,
        details = fields.details.value.allDetails,
        lifestyle = fields.lifeStyle.value.toDomain().toPersistentList(),
        interests = fields.interests.value.value,
        languages = fields.language.value.toDomain()
    )

    sealed interface SaveState {
        object Idle : SaveState
        object Loading : SaveState
        object Error : SaveState
    }
}

data class ProfileFields(
    val name: ProfileField<String>,
    val birthday: ProfileField<LocalDate>,
    val images: ProfileField<PersistentList<Image>>,
    val aboutMe: ProfileField<String>,
    val lookingFor: ProfileField<LookingForData>,
    val details: ProfileField<DetailFields>,
    val lifeStyle: ProfileField<LifestyleFields>,
    val interests: ProfileField<AssetsFromDb>,
    val language: ProfileField<LanguagesAssetsFields>,
    val mainAchievement: ProfileField<Achievement?>,
    val achievements: ProfileField<PersistentList<Achievement>>
)

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
    val fields = ProfileFields(
        name = name.toField(),
        birthday = birthday.toField(),
        images = images.toPersistentList().toField(),
        aboutMe = aboutMe.toField(),
        lookingFor = lookingFor.toField(),
        details = details.toDetailFields().toField(),
        mainAchievement = mainAchievement.toField(),
        achievements = achievements.toPersistentList().toField(),
        lifeStyle = lifestyles.toLifestyleFields().toField(),
        interests = interests.toPersistentList().run(::createInterests).toField(),
        language = languagesAsset.toLanguagesFields().toField()
    )
    return ProfileEditorState(
        fields = fields,
        fieldsChanged = false
    )
}

fun Profile.updated(state: ProfileEditorState) = copy(
    name = state.fields.name.value,
    birthday = state.fields.birthday.value,
    images = state.fields.images.value,
    aboutMe = state.fields.aboutMe.value,
    lookingFor = state.fields.lookingFor.value,
    details = state.fields.details.value.allDetails,
    lifestyles = state.fields.lifeStyle.value.toDomain(),
    interests = state.fields.interests.value.value,
    languagesAsset = state.fields.language.value.toDomain(),
    mainAchievement = state.fields.mainAchievement.value,
    achievements = state.fields.achievements.value
)

fun Profile.updated(fields: ProfileFields) = copy(
    name = fields.name.value,
    birthday = fields.birthday.value,
    images = fields.images.value,
    aboutMe = fields.aboutMe.value,
    lookingFor = fields.lookingFor.value,
    details = fields.details.value.allDetails,
    lifestyles = fields.lifeStyle.value.toDomain(),
    interests = fields.interests.value.value,
    languagesAsset = fields.language.value.toDomain(),
    mainAchievement = fields.mainAchievement.value,
    achievements = fields.achievements.value
)

private fun <T> T.toField(): ProfileField<T> {
    return ProfileField(this)
}

private fun createInterests(value: PersistentList<Text>): AssetsFromDb {
    return AssetsFromDb(
        value = value,
        maxValues = 10,
        foundedAssets = FoundedAssets.Searching,
        searchItem = SearchItem("")
    )
}
