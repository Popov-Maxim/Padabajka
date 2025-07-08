package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.DetailType
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField
import kotlinx.collections.immutable.PersistentList

@Composable
fun DetailsBlock(
    field: ProfileField<PersistentList<Detail>>,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val supportedDetails = field.value.getSupportedDetails()
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        City(supportedDetails.city, onEvent)
        SexualOrientation(supportedDetails.sexualOrientation, onEvent)
        Education(supportedDetails.education, onEvent)
        Profession(supportedDetails.profession, onEvent)
        Height(supportedDetails.height, onEvent)
        Character(supportedDetails.character, onEvent)
        Language(supportedDetails.language, onEvent)
    }
}

@Composable
private fun City(
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val valueText = detail?.value?.toText() ?: ""

    SmallEditField(
        label = StaticTextId.UiId.City.translate(),
        value = valueText,
        hint = "lol",
        onChange = { fieldValue ->
            onChange(fieldValue, DetailType.City, detail, onEvent)
        }
    )
}

@Composable
private fun SexualOrientation(
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val valueText = detail?.value?.toText() ?: ""

    SmallEditField(
        label = StaticTextId.UiId.SexualOrientation.translate(),
        value = valueText,
        hint = "lol",
        onChange = { fieldValue ->
            onChange(fieldValue, DetailType.SexualOrientation, detail, onEvent)
        }
    )
}

@Composable
private fun Education(
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val valueText = detail?.value?.toText() ?: ""

    SmallEditField(
        label = StaticTextId.UiId.Education.translate(),
        value = valueText,
        hint = "lol",
        onChange = { fieldValue ->
            onChange(fieldValue, DetailType.Education, detail, onEvent)
        }
    )
}

@Composable
private fun Profession(
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val valueText = detail?.value?.toText() ?: ""

    SmallEditField(
        label = StaticTextId.UiId.Profession.translate(),
        value = valueText,
        hint = "lol",
        onChange = { fieldValue ->
            onChange(fieldValue, DetailType.Profession, detail, onEvent)
        }
    )
}

@Composable
private fun Height(
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val valueText = detail?.value?.toText() ?: ""

    SmallEditField(
        label = StaticTextId.UiId.Height.translate(),
        value = valueText,
        hint = "lol",
        onChange = { fieldValue ->
            onChange(fieldValue, DetailType.Height, detail, onEvent)
        }
    )
}

@Composable
private fun Character(
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val valueText = detail?.value?.toText() ?: ""

    SmallEditField(
        label = StaticTextId.UiId.Character.translate(),
        value = valueText,
        hint = "lol",
        onChange = { fieldValue ->
            onChange(fieldValue, DetailType.Character, detail, onEvent)
        }
    )
}

@Composable
private fun Language(
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val valueText = detail?.value?.toText() ?: ""

    SmallEditField(
        label = StaticTextId.UiId.Language.translate(),
        value = valueText,
        hint = "lol",
        onChange = { fieldValue ->
            onChange(fieldValue, DetailType.Language, detail, onEvent)
        }
    )
}

@Composable
private fun Detail.Value.toText(): String {
    return when (this) {
        is Detail.Value.Centimeter -> raw.toString()
        is Detail.Value.String -> raw
        is Detail.Value.Asset -> raw.translate()
    }
}

@Stable
fun PersistentList<Detail>.getSupportedDetails(): SupportedDetails {
    var city: Detail? = null
    var sexualOrientation: Detail? = null
    var education: Detail? = null
    var profession: Detail? = null
    var height: Detail? = null
    var character: Detail? = null
    var language: Detail? = null

    onEach { detail ->
        when (detail.type) {
            DetailType.City -> city = detail
            DetailType.SexualOrientation -> sexualOrientation = detail
            DetailType.Education -> education = detail
            DetailType.Profession -> profession = detail
            DetailType.Height -> height = detail
            DetailType.Character -> character = detail
            DetailType.Language -> language = detail
            else -> Unit // Handle other types or ignore
        }
    }

    return SupportedDetails(
        city = city,
        sexualOrientation = sexualOrientation,
        education = education,
        profession = profession,
        height = height,
        character = character,
        language = language,
    )
}

private fun onChange(
    fieldValue: String,
    detailType: String,
    detail: Detail?,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val newValue = Detail.Value.String(fieldValue)
    val newDetail = detail?.copy(value = newValue)
        ?: Detail(detailType, newValue, null)
    onEvent(DetailUpdateEvent(newDetail))
}

data class SupportedDetails(
    val city: Detail?,
    val sexualOrientation: Detail?,
    val education: Detail?,
    val profession: Detail?,
    val height: Detail?,
    val character: Detail?,
    val language: Detail?
)
