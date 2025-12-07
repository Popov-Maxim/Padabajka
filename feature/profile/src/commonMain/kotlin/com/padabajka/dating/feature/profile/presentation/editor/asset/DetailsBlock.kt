package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailFields
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUIItem
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField

@Composable
fun DetailsBlock(
    field: ProfileField<DetailFields>,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val supportedDetails = field.value.supportedDetails
    var bottomSheetTabState: DetailTab? by remember { mutableStateOf(null) }
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        City(supportedDetails.city) { bottomSheetTabState = DetailTab.City }
        SexualOrientation(supportedDetails.sexualOrientation) { bottomSheetTabState = DetailTab.SexualOrientation }
        Profession(supportedDetails.profession) { bottomSheetTabState = DetailTab.Profession }
        HeightField(supportedDetails.height) { bottomSheetTabState = DetailTab.Height }
        Character(supportedDetails.character) { bottomSheetTabState = DetailTab.Character }
    }

    bottomSheetTabState?.let { state ->
        DetailsEditorBottomSheet(
            tabsState = state,
            supportedDetails = supportedDetails,
            onEvent = onEvent,
            onTabSelected = {
                bottomSheetTabState = it
            },
            onDismissRequest = {
                bottomSheetTabState = null
            }
        )
    }
}

@Composable
private fun City(
    detail: DetailUIItem.AssetFromDb,
    onClick: () -> Unit
) {
    val valueText = detail.getRawValue() ?: ""

    SmallOnlyDataField(
        label = StaticTextId.UiId.City.translate(),
        value = valueText,
        hint = StaticTextId.UiId.CityHint.translate(),
        onClick = onClick
    )
}

@Composable
private fun SexualOrientation(
    detail: DetailUIItem.Asset,
    onClick: () -> Unit
) {
    val valueText = detail.getRawValue() ?: ""

    SmallOnlyDataField(
        label = StaticTextId.UiId.SexualOrientation.translate(),
        value = valueText,
        hint = StaticTextId.UiId.SexualOrientationHint.translate(),
        onClick = onClick
    )
}

@Composable
private fun Profession(
    detail: DetailUIItem.TextField,
    onClick: () -> Unit
) {
    val valueText = detail.getRawValue() ?: ""

    SmallOnlyDataField(
        label = StaticTextId.UiId.Profession.translate(),
        value = valueText,
        hint = StaticTextId.UiId.ProfessionHint.translate(),
        onClick = onClick
    )
}

@Composable
private fun HeightField(
    detail: DetailUIItem.Int,
    onClick: () -> Unit
) {
    val valueText = detail.getRawValue() ?: ""

    SmallOnlyDataField(
        label = StaticTextId.UiId.Height.translate(),
        value = valueText,
        hint = StaticTextId.UiId.HeightHint.translate(),
        onClick = onClick
    )
}

@Composable
private fun Character(
    detail: DetailUIItem.Asset,
    onClick: () -> Unit
) {
    val valueText = detail.getRawValue() ?: ""

    SmallOnlyDataField(
        label = StaticTextId.UiId.Character.translate(),
        value = valueText,
        hint = StaticTextId.UiId.CharacterHint.translate(),
        onClick = onClick
    )
}

@Composable
private fun DetailUIItem.getRawValue(): String? {
    return when (this) {
        is DetailUIItem.Asset -> value?.translate()
        is DetailUIItem.AssetFromDb -> value?.translate()
        is DetailUIItem.Int -> value?.toString()
        is DetailUIItem.TextField -> value
    }
}
