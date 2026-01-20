package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.CoreChipItem
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.presentation.ui.layout.SeparatedColumn
import com.padabajka.dating.core.presentation.ui.layout.SeparatedColumnScope
import com.padabajka.dating.core.presentation.ui.modifier.optionalClickable
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.feature.profile.presentation.editor.model.LangSearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguageAssetsField
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesAssetType
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesAssetsFields
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateLangSearchEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.isEmpty
import com.padabajka.dating.feature.profile.presentation.model.AssetsFromDb

@Composable
fun LanguageBlock(
    field: ProfileField<LanguagesAssetsFields>,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val languageAssetsFields = field.value
    var selectedType: LanguagesAssetType? by remember { mutableStateOf(null) }

    Column {
        Languages(
            title = StaticTextId.UiId.Native,
            field = languageAssetsFields.nativeLanguages,
            onClick = { selectedType = LanguagesAssetType.Native }
        )
        LifestyleDivider()
        Languages(
            title = StaticTextId.UiId.Known,
            field = languageAssetsFields.knownLanguages,
            onClick = { selectedType = LanguagesAssetType.Known }
        )
        LifestyleDivider()
        Languages(
            title = StaticTextId.UiId.Learning,
            field = languageAssetsFields.learningLanguages,
            onClick = { selectedType = LanguagesAssetType.Learning }
        )
    }

    selectedType?.let { type ->
        LaunchedEffect(type) {
            onEvent(UpdateLangSearchEvent(type))
        }

        val languageAssetsField = when (type) {
            LanguagesAssetType.Native -> languageAssetsFields.nativeLanguages
            LanguagesAssetType.Known -> languageAssetsFields.knownLanguages
            LanguagesAssetType.Learning -> languageAssetsFields.learningLanguages
        }

        val title = when (type) {
            LanguagesAssetType.Native -> StaticTextId.UiId.YourNativeLanguage.translate()
            LanguagesAssetType.Known -> StaticTextId.UiId.YourKnownLanguage.translate()
            LanguagesAssetType.Learning -> StaticTextId.UiId.YourLearningLanguage.translate()
        }
        val body = StaticTextId.UiId.SelectUpToNLanguages.translate()
            .replace("{n}", languageAssetsField.maxValues.toString())

        val textAssetUIText = TextAssetUIText(
            title = title,
            body = body
        )

        AssetSelectorEditorBottomSheet(
            assetsFromDb = AssetsFromDb(
                value = languageAssetsField.value,
                maxValues = languageAssetsField.maxValues,
                foundedAssets = languageAssetsField.foundedAssets,
                searchItem = languageAssetsField.searchItem
            ),
            textAssetUIText = textAssetUIText,
            onChangeValue = { value ->
                onEvent(LanguagesUpdateEvent(languageAssetsField.copy(value = value), type))
            },
            onChangeSearchField = {
                onEvent(LangSearchQueryChangedEvent(it, type))
            },
            onDismissRequest = {
                selectedType = null
            }
        )
    }
}

@Composable
fun LanguageBlockView(field: LanguagesAssetsFields) {
    val languageAssetsFields = field

    SeparatedColumn(
        divider = {
            LifestyleDivider()
        }
    ) {
        itemLanguageField(languageAssetsFields.nativeLanguages) {
            Languages(
                title = StaticTextId.UiId.Native,
                field = languageAssetsFields.nativeLanguages,
            )
        }
        itemLanguageField(languageAssetsFields.knownLanguages) {
            Languages(
                title = StaticTextId.UiId.Known,
                field = languageAssetsFields.knownLanguages,
            )
        }
        itemLanguageField(languageAssetsFields.learningLanguages) {
            Languages(
                title = StaticTextId.UiId.Learning,
                field = languageAssetsFields.learningLanguages,
            )
        }
    }
}

private fun SeparatedColumnScope.itemLanguageField(
    field: LanguageAssetsField,
    content: @Composable () -> Unit
) {
    if (field.isEmpty().not()) {
        item(content = content)
    }
}

@Composable
private fun LifestyleDivider() {
    Box(modifier = Modifier.padding(horizontal = 10.dp)) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black.copy(alpha = 0.25f)
        )
    }
}

@Composable
private fun Languages(
    title: StaticTextId,
    field: LanguageAssetsField,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.optionalClickable(onClick = onClick)
            .padding(vertical = 20.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(text = title.translate())
        Languages(
            field = field,
            showAddButton = onClick != null
        )
    }
}

@Composable
private fun Languages(
    field: LanguageAssetsField,
    showAddButton: Boolean = true
) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        field.value.onEach {
            CoreChipItem(
                text = it.translate()
            )
        }
        if (field.value.size < field.maxValues && showAddButton) {
            CoreChipItem(
                text = StaticTextId.UiId.Add.translate(),
                icon = CoreIcons.Add.toData(),
                colorGroup = CoreColors.Chips.unselected,
                border = BorderStroke(1.dp, CoreColors.Chips.unselected.textColor)
            )
        }
    }
}
