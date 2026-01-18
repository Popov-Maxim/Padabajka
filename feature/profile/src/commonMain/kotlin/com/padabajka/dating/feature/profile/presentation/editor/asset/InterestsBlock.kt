package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.CoreChipItem
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.presentation.ui.modifier.optionalClickable
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.feature.profile.presentation.editor.model.InterestSearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.InterestUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateInterestSearchEvent
import com.padabajka.dating.feature.profile.presentation.model.AssetsFromDb

@Composable
fun InterestsBlock(
    field: ProfileField<AssetsFromDb>,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    var showSelector: Boolean by remember { mutableStateOf(false) }
    Interests(
        field = field.value,
        onClick = { showSelector = true }
    )

    if (showSelector) {
        LaunchedEffect(Unit) {
            onEvent(UpdateInterestSearchEvent)
        }

        val body = StaticTextId.UiId.SelectUpToNInterests.translate()
            .replace("{n}", field.value.maxValues.toString())

        val textAssetUIText = TextAssetUIText(
            title = StaticTextId.UiId.YourInterests.translate(),
            body = body
        )

        AssetSelectorEditorBottomSheet(
            assetsFromDb = field.value,
            textAssetUIText = textAssetUIText,
            onChangeValue = {
                onEvent(InterestUpdateEvent(it))
            },
            onChangeSearchField = {
                onEvent(InterestSearchQueryChangedEvent(it))
            },
            onDismissRequest = {
                showSelector = false
            }
        )
    }
}

@Composable
fun Interests(
    field: AssetsFromDb,
    onClick: (() -> Unit)? = null
//    onEvent: (ProfileEditorEvent) -> Unit
) {
    Row(
        modifier = Modifier.optionalClickable(onClick = onClick)
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        FlowRow(
//            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            field.value.onEach {
                CoreChipItem(
                    text = it.translate()
                )
            }
            val maxValue = field.maxValues
            val dontFill = maxValue == null || field.value.size < maxValue
            val showAddButton = onClick != null
            if (dontFill && showAddButton) {
                CoreChipItem(
                    text = StaticTextId.UiId.Add.translate(),
                    icon = CoreIcons.Add.toData(),
                    colorGroup = CoreColors.Chips.unselected,
                    border = BorderStroke(1.dp, CoreColors.Chips.unselected.textColor)
                )
            }
        }
    }
}
