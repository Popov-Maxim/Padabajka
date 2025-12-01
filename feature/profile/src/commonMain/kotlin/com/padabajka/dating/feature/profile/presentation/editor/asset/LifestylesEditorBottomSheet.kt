package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.presentation.ui.toPx
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset
import com.padabajka.dating.feature.profile.presentation.editor.model.LifeStyleUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LifestyleField
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedLifestyles
import com.padabajka.dating.feature.profile.presentation.editor.model.updateAttributes
import com.padabajka.dating.feature.profile.presentation.editor.model.updateValue
import kotlinx.collections.immutable.PersistentList
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifestylesEditorBottomSheet(
    tabsState: LifestyleTab,
    supportedDetails: SupportedLifestyles,
    onEvent: (ProfileEditorEvent) -> Unit,
    onTabSelected: (LifestyleTab) -> Unit,
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
        var supportedDetailsState by remember(key1 = tabsState, key2 = supportedDetails) {
            mutableStateOf(supportedDetails)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 25.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            NavigateTabs(tabsState, onTabSelected)
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black.copy(alpha = 0.18f)
            )
            Content(
                modifier = Modifier.fillMaxWidth().weight(1f),
                tabsState = tabsState,
                supportedLifestyles = supportedDetailsState,
                onChange = { supportedDetailsState = it },
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black.copy(alpha = 0.18f)
            )
            SaveButton(
                onClick = {
                    onEvent(LifeStyleUpdateEvent(supportedDetailsState))

                    val index = LifestyleTab.entries.indexOf(tabsState)
                    val nextTab = LifestyleTab.entries.getOrNull(index + 1)
                    if (nextTab != null) {
                        onTabSelected(nextTab)
                    } else {
                        onDismissRequest()
                    }
                }
            )
        }
    }
}

@Composable
private fun NavigateTabs( // TODO: copy from DetailsEditorBottomSheet.kt
    tabsState: LifestyleTab,
    changeState: (LifestyleTab) -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val scrollState = rememberScrollState()

    data class TabInfo(val left: Int, val width: Int)

    val tabPositions = remember { mutableStateMapOf<LifestyleTab, TabInfo>() }

    var viewportWidth by remember { mutableStateOf(0) }
    val horizontalPadding = 20.dp
    val contentRowSize = viewportWidth - horizontalPadding.toPx() * 2

    LaunchedEffect(tabsState, tabPositions, contentRowSize) {
        tabPositions[tabsState]?.let { info ->
            val tabCenter = info.left + info.width / 2
            val target = tabCenter - contentRowSize / 2
            val targetInt = target.coerceIn(0, scrollState.maxValue)
            scrollState.animateScrollTo(targetInt)
        }
    }

    Row(
        modifier = Modifier
            .onGloballyPositioned { coords ->
                viewportWidth = coords.size.width
            }
//            .horizontalScroll(scrollState)
            .fillMaxWidth()
            .wrapContentWidth(unbounded = true)
            .padding(horizontal = horizontalPadding)
            .background(Color(color = 0xFFD9D9D9), shape),
    ) {
        LifestyleTab.entries.forEach { tab ->
            val isSelected = tab == tabsState
            val contentColor = if (isSelected) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .clip(shape)
                    .clickable {
                        changeState(tab)
                    }
                    .background(if (isSelected) CoreColors.secondary.mainColor else Color.Transparent)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .onGloballyPositioned { coords ->
                        val pos = coords.positionInParent()
                        tabPositions[tab] =
                            TabInfo(left = pos.x.roundToInt(), width = coords.size.width)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.staticId.translate(),
                    maxLines = 1,
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
private fun Content(
    tabsState: LifestyleTab,
    supportedLifestyles: SupportedLifestyles,
    modifier: Modifier = Modifier,
    onChange: (SupportedLifestyles) -> Unit
) {
    when (tabsState) {
        LifestyleTab.Smoking -> Smoking(
            modifier = modifier,
            lifestyleField = supportedLifestyles.smoking,
            onChange = { field ->
                val newSupportedLifestyles = supportedLifestyles.copy(smoking = field)
                onChange(newSupportedLifestyles)
            }
        )
        LifestyleTab.Alcohol -> Alcohol(
            modifier = modifier,
            lifestyleField = supportedLifestyles.alcohol,
            onChange = { field ->
                val newSupportedLifestyles = supportedLifestyles.copy(alcohol = field)
                onChange(newSupportedLifestyles)
            }
        )
        LifestyleTab.Animals -> Animals(
            modifier = modifier,
            lifestyleField = supportedLifestyles.animals,
            onChange = { field ->
                val newSupportedLifestyles = supportedLifestyles.copy(animals = field)
                onChange(newSupportedLifestyles)
            }
        )
    }
}

@Composable
private fun Smoking(
    modifier: Modifier,
    lifestyleField: LifestyleField,
    onChange: (LifestyleField) -> Unit,
) {
    TotalLifestyleBlock(
        modifier = modifier,
        title = StaticTextId.UiId.Smoking.translate(),
        description = StaticTextId.UiId.SmokingHint.translate(),
        lifestyleField = lifestyleField,
        onChange = { newValue ->
            onChange(newValue)
        }
    )
}

@Composable
private fun Alcohol(
    modifier: Modifier,
    lifestyleField: LifestyleField,
    onChange: (LifestyleField) -> Unit,
) {
    TotalLifestyleBlock(
        modifier = modifier,
        title = StaticTextId.UiId.Alcohol.translate(),
        description = StaticTextId.UiId.AlcoholHint.translate(),
        lifestyleField = lifestyleField,
        onChange = { newValue ->
            onChange(newValue)
        }
    )
}

@Composable
private fun Animals(
    modifier: Modifier,
    lifestyleField: LifestyleField,
    onChange: (LifestyleField) -> Unit,
) {
    TotalLifestyleBlock(
        modifier = modifier,
        title = StaticTextId.UiId.Animals.translate(),
        description = StaticTextId.UiId.AnimalsHint.translate(),
        lifestyleField = lifestyleField,
        onChange = { newValue ->
            onChange(newValue)
        }
    )
}

@Composable
private fun TotalLifestyleBlock(
    modifier: Modifier,
    title: String,
    description: String,
    lifestyleField: LifestyleField,
    onChange: (LifestyleField) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextAsset(
                title = title,
                body = description,
                modifier = Modifier.fillMaxWidth(),
                titleFontSize = 24.sp,
                bodyFontSize = 16.sp,
            )
        }

        ValueBlock(
            modifier = Modifier.fillMaxWidth(),
            selectedAsset = lifestyleField.value.value,
            possibleAssets = lifestyleField.possibleValues.values,
            onChange = {
                val newField = lifestyleField.updateValue(value = it)
                onChange(newField)
            }
        )

        AttributesBlock(
            modifier = Modifier.fillMaxWidth(),
            selectedAssets = lifestyleField.value.attributes,
            possibleAssets = lifestyleField.possibleValues.attributes,
            onChange = {
                val newField = lifestyleField.updateAttributes(attributes = it)
                onChange(newField)
            }
        )
    }
}

@Composable
private fun ValueBlock(
    selectedAsset: Text?,
    modifier: Modifier = Modifier,
    possibleAssets: PersistentList<StaticTextId>,
    onChange: (selectedAsset: Text?) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = StaticTextId.UiId.YourLifestyle.translate(),
            color = CoreColors.background.textColor.copy(alpha = 0.6f)
        )
        CoreSingleSmallAssetsSelection(
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
            paddingForItem = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
            selectedAsset = selectedAsset,
            possibleAssets = possibleAssets,
            onChange = onChange,
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
            verticalArrangement = Arrangement.spacedBy(space = 15.dp)
        )
    }
}

@Composable
private fun AttributesBlock(
    selectedAssets: PersistentList<Text>,
    modifier: Modifier = Modifier,
    possibleAssets: PersistentList<StaticTextId>,
    onChange: (assets: PersistentList<Text>) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = StaticTextId.UiId.ChooseTheAppropriateOne.translate(),
            color = CoreColors.background.textColor.copy(alpha = 0.6f)
        )
        CoreMultiSmallAssetsSelection(
            modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
            paddingForItem = PaddingValues(horizontal = 15.dp, vertical = 10.dp),
            selectedAssets = selectedAssets,
            possibleAssets = possibleAssets,
            onChange = onChange,
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp),
            verticalArrangement = Arrangement.spacedBy(space = 15.dp)
        )
    }
}

enum class LifestyleTab(val staticId: StaticTextId) {
    Smoking(StaticTextId.UiId.Smoking),
    Alcohol(StaticTextId.UiId.Alcohol),
    Animals(StaticTextId.UiId.Animals);

    companion object {
        private val mapper = entries.associateBy { it.staticId }

        fun parse(id: StaticTextId): LifestyleTab? = mapper[id]
    }
}
