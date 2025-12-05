package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.modifier.optionalClickable
import com.padabajka.dating.core.repository.api.model.profile.Text
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoreSmallAssetsSelection(
    selectedAssets: PersistentList<Text>,
    possibleAssets: PersistentList<Text>,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    paddingForItem: PaddingValues,
    modifier: Modifier = Modifier,
    maxSelectedItems: Int? = null,
    onClick: (asset: Text) -> Unit
) {
    val selectedAssetIds = selectedAssets.map { it.id }.toImmutableSet()
    val shape = RoundedCornerShape(20.dp)
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) {
        val enabledUnselected = maxSelectedItems == null || selectedAssets.size < maxSelectedItems
        possibleAssets.onEach { asset ->
            val isSelected = asset.id in selectedAssetIds
            val withBackground: Modifier.() -> Modifier = {
                if (isSelected) {
                    background(CoreColors.secondary.mainColor)
                } else if (enabledUnselected.not()) {
                    background(Color.LightGray) // TODO: change color
                } else {
                    this
                }
            }
            val withBorder: Modifier.() -> Modifier = {
                if (isSelected.not() && enabledUnselected) border(0.5.dp, Color.Black, shape) else this
            }
            val contentColor = if (isSelected) {
                CoreColors.secondary.textColor
            } else {
                CoreColors.background.textColor
            }
            Box(
                modifier = Modifier
                    .withBorder()
                    .clip(shape)
                    .optionalClickable(
                        enabled = isSelected || enabledUnselected,
                        onClick = { onClick(asset) }
                    )
                    .withBackground()
                    .padding(paddingForItem)
            ) {
                Text(
                    text = asset.translate(),
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun CoreSingleSmallAssetsSelection(
    selectedAsset: Text?,
    possibleAssets: PersistentList<Text>,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    paddingForItem: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
    allowDelete: Boolean = true,
    modifier: Modifier = Modifier,
    onChange: (asset: Text?) -> Unit
) {
    CoreSmallAssetsSelection(
        selectedAssets = listOfNotNull(selectedAsset).toPersistentList(),
        possibleAssets = possibleAssets,
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        paddingForItem = paddingForItem
    ) { asset ->
        val value = if (asset.id != selectedAsset?.id || allowDelete.not()) {
            asset
        } else {
            null
        }
        onChange(value)
    }
}

@Composable
fun CoreMultiSmallAssetsSelection(
    selectedAssets: PersistentList<Text>,
    possibleAssets: PersistentList<Text>,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    maxSelectedItems: Int? = null,
    paddingForItem: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
    modifier: Modifier = Modifier,
    onChange: (assets: PersistentList<Text>) -> Unit
) {
    val selectedAssetIds = selectedAssets.map { it.id }.toImmutableSet()
    CoreSmallAssetsSelection(
        selectedAssets = selectedAssets,
        possibleAssets = possibleAssets,
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        paddingForItem = paddingForItem,
        maxSelectedItems = maxSelectedItems
    ) { text ->
        val newAssets = if (text.id in selectedAssetIds) {
            selectedAssets.removeAll { it.id == text.id }
        } else {
            val asset = text
            selectedAssets.add(asset)
        }
        onChange(newAssets)
    }
}
