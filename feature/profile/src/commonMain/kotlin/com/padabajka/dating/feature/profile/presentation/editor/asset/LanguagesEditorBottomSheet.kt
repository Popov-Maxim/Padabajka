package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CoreMultiSmallAssetsSelection
import com.padabajka.dating.core.presentation.ui.CoreSearchField
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset
import com.padabajka.dating.feature.profile.presentation.editor.model.FoundedAssets
import com.padabajka.dating.feature.profile.presentation.editor.model.LangSearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguageAssetsField
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesAssetType
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SearchItem
import kotlinx.collections.immutable.PersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagesEditorBottomSheet(
    languageAssetsField: LanguageAssetsField,
    type: LanguagesAssetType,
    languageUIText: LanguageUIText,
    onChange: (LanguageAssetsField) -> Unit,
    onEvent: (ProfileEditorEvent) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = CoreColors.background.mainColor,
//        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        contentWindowInsets = { WindowInsets.safeDrawing },
        dragHandle = null // TODO add dragHandle
    ) {
        val foundedAssets by remember(languageAssetsField.foundedAssets) {
            mutableStateOf(languageAssetsField.foundedAssets)
        }
        var supportedDetailsState by remember(languageAssetsField.value) { // TODO: bad
            mutableStateOf(languageAssetsField)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            SearchBlock(
                languageAssetsField.searchItem,
                type,
                languageUIText,
                onEvent
            )
            FoundedAssets(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                value = supportedDetailsState.value,
                maxSelectedItems = languageAssetsField.maxValues,
                foundedAssets = foundedAssets,
                onChange = {
                    supportedDetailsState = supportedDetailsState.copy(value = it)
                }
            )
            SaveButton(
                onClick = {
                    onChange(supportedDetailsState)
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
private fun SearchBlock(
    searchItem: SearchItem,
    type: LanguagesAssetType,
    languageUIText: LanguageUIText,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextAsset(
            title = languageUIText.title,
            body = languageUIText.body,
            titleFontSize = 24.sp,
            bodyFontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )
        CoreSearchField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            text = searchItem.value,
            onChange = {
                onEvent(LangSearchQueryChangedEvent(it, type))
            },
            hint = StaticTextId.UiId.LanguageSearch.translate()
        )
    }
}

@Composable
private fun FoundedAssets(
    value: PersistentList<Text>,
    foundedAssets: FoundedAssets,
    maxSelectedItems: Int?,
    modifier: Modifier = Modifier,
    onChange: (assets: PersistentList<Text>) -> Unit,
) {
    Column(modifier = modifier) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black.copy(alpha = 0.18f)
        )
        val scrollState = rememberScrollState()
        val modifierContent = Modifier.fillMaxWidth().weight(1f)
            .padding(horizontal = 15.dp, vertical = 40.dp)
        when (foundedAssets) {
            FoundedAssets.Searching -> {
                Box(modifier = modifierContent, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }

            is FoundedAssets.Success -> {
                CoreMultiSmallAssetsSelection(
                    modifier = modifierContent.verticalScroll(scrollState),
                    paddingForItem = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
                    selectedAssets = value,
                    possibleAssets = foundedAssets.possibleAssets,
                    onChange = onChange,
                    maxSelectedItems = maxSelectedItems,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.Start
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 15.dp,
                        alignment = Alignment.Top
                    )
                )
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black.copy(alpha = 0.18f)
        )
    }
}

data class LanguageUIText(
    val title: String,
    val body: String,
)
