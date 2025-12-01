package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import com.padabajka.dating.core.presentation.ui.CoreTextEditField
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.toText
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.presentation.ui.toPx
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.presentation.creator.name.TextAsset
import com.padabajka.dating.feature.profile.presentation.editor.asset.picker.TickWheelPicker
import com.padabajka.dating.feature.profile.presentation.editor.model.CitySearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUIItem
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.FoundedAssets
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedDetails
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateCitySearchEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.updatedCharacter
import com.padabajka.dating.feature.profile.presentation.editor.model.updatedCity
import com.padabajka.dating.feature.profile.presentation.editor.model.updatedHeight
import com.padabajka.dating.feature.profile.presentation.editor.model.updatedProfession
import com.padabajka.dating.feature.profile.presentation.editor.model.updatedSexualOrientation
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsEditorBottomSheet(
    tabsState: DetailTab,
    supportedDetails: SupportedDetails,
    onEvent: (ProfileEditorEvent) -> Unit,
    onTabSelected: (DetailTab) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = CoreColors.background.mainColor,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        dragHandle = null // TODO add dragHandle
    ) {
        var supportedDetailsState by remember(key1 = tabsState, key2 = supportedDetails) {
            mutableStateOf(supportedDetails)
        }

        Column(
            modifier = Modifier.fillMaxHeight(fraction = 2f / 3).padding(top = 25.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            NavigateTabs(tabsState, onTabSelected)
            Content(
                modifier = Modifier.weight(1f),
                tabsState = tabsState,
                supportedDetails = supportedDetailsState,
                onChange = { supportedDetailsState = it },
                onEvent = onEvent
            )
            SaveButton(
                onClick = {
                    onEvent(DetailUpdateEvent(supportedDetailsState))

                    val index = DetailTab.entries.indexOf(tabsState)
                    val nextTab = DetailTab.entries.getOrNull(index + 1)
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
fun SaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(horizontal = 30.dp)
            .fillMaxWidth()
    ) {
        val shape = RoundedCornerShape(20.dp)
        Box(
            modifier = Modifier.background(CoreColors.secondary.mainColor, shape)
                .clip(shape)
                .clickable { onClick() }
                .padding(vertical = 15.dp).fillMaxWidth()
        ) {
            Text(
                text = StaticTextId.UiId.Apply.translate(),
                color = CoreColors.secondary.textColor,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun NavigateTabs(
    tabsState: DetailTab,
    changeState: (DetailTab) -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val scrollState = rememberScrollState()

    data class TabInfo(val left: Int, val width: Int)

    val tabPositions = remember { mutableStateMapOf<DetailTab, TabInfo>() }

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
            .horizontalScroll(scrollState)
            .fillMaxWidth()
            .wrapContentWidth(unbounded = true)
            .padding(horizontal = horizontalPadding)
            .background(Color(color = 0xFFD9D9D9), shape),
    ) {
        DetailTab.entries.forEach { tab ->
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
    tabsState: DetailTab,
    supportedDetails: SupportedDetails,
    modifier: Modifier = Modifier,
    onEvent: (ProfileEditorEvent) -> Unit,
    onChange: (SupportedDetails) -> Unit
) {
    when (tabsState) {
        DetailTab.Height -> Height(
            modifier = modifier,
            heightItem = supportedDetails.height,
            onChange = { height ->
                onChange(supportedDetails.updatedHeight { height })
            },
        )

        DetailTab.City -> City(
            modifier = modifier,
            cityItem = supportedDetails.city,
            onEvent = onEvent,
            onSelected = {
                onChange(supportedDetails.updatedCity { it })
            }
        )

        DetailTab.SexualOrientation -> SmallAssetsSelection(
            modifier = modifier,
            detailUIItem = supportedDetails.sexualOrientation,
            onChange = {
                onChange(supportedDetails.updatedSexualOrientation { it })
            }
        )

        DetailTab.Character -> SmallAssetsSelection(
            modifier = modifier,
            detailUIItem = supportedDetails.character,
            onChange = {
                onChange(supportedDetails.updatedCharacter { it })
            }
        )

        DetailTab.Profession -> ProfessionSelector(
            modifier = modifier,
            value = supportedDetails.profession.value ?: "",
            onSelected = {
                onChange(supportedDetails.updatedProfession { copy(value = it) })
            }
        )

        else -> Box(modifier)
    }
}

@Composable
private fun Height(
    heightItem: DetailUIItem.Int,
    onChange: (DetailUIItem.Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val height = heightItem.value
    Box(
        modifier = modifier.fillMaxSize().padding(horizontal = 25.dp, vertical = 100.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TickWheelPicker(
                modifier = Modifier.fillMaxWidth(fraction = 0.65f),
                selectedValue = height ?: DEFAULT_HEIGHT,
                onValueChange = {
                    onChange(heightItem.copy(value = it))
                }
            )
            CoreTextEditField(
                modifier = Modifier.width(100.dp),
                text = height?.toString() ?: "",
                onChange = {
                    onChange(heightItem.copy(value = it.toIntOrNull()))
                },
            )
        }
    }
}

@Composable
private fun City(
    cityItem: DetailUIItem.AssetFromDb,
    onEvent: (ProfileEditorEvent) -> Unit,
    onSelected: (DetailUIItem.AssetFromDb) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        onEvent(UpdateCitySearchEvent)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CoreTextEditField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            text = cityItem.searchItem.value,
            onChange = {
                onEvent(CitySearchQueryChangedEvent(it))
            },
            hint = StaticTextId.UiId.CitySearch.translate()
        )
        when (val foundedAssets = cityItem.foundedAssets) {
            FoundedAssets.Searching -> {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }

            is FoundedAssets.Success -> {
                CitiesList(foundedAssets) {
                    onSelected(cityItem.copy(value = it))
                }
            }
        }
    }
}

@Composable
private fun CitiesList(
    foundedAssets: FoundedAssets.Success,
    onSelected: (Text) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().innerShadow()) {
        val possibleAssets = foundedAssets.possibleAssets
        itemsIndexed(possibleAssets) { index, city ->
            Box(
                modifier = Modifier
                    .clickable {
                        onSelected(city)
                    }
                    .fillMaxWidth().padding(horizontal = 20.dp, vertical = 15.dp)
            ) {
                Text(
                    text = city.translate(),
                    fontSize = 15.sp
                )
            }

            if (index != possibleAssets.lastIndex) {
                Box(Modifier.padding(horizontal = 10.dp)) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black.copy(alpha = 0.25f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfessionSelector(
    value: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 30.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        TextAsset(
            title = StaticTextId.UiId.ProfessionSelectorTitle.translate(),
            body = StaticTextId.UiId.ProfessionSelectorBody.translate(),
            modifier = Modifier.fillMaxWidth()
        )

        CoreTextEditField(
            text = value,
            singleLine = true,
            hint = StaticTextId.UiId.ProfessionSelectorHint.translate(),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 45.dp),
            onChange = { onSelected(it) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CoreSmallAssetsSelection(
    selectedAssets: PersistentList<Text>,
    possibleAssets: PersistentList<StaticTextId>,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    paddingForItem: PaddingValues,
    modifier: Modifier = Modifier,
    onClick: (asset: StaticTextId) -> Unit
) {
    val selectedAssetIds = selectedAssets.map { it.id.raw }.toImmutableSet()
    val shape = RoundedCornerShape(20.dp)
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) {
        possibleAssets.onEach { asset ->
            val isSelected = asset.id in selectedAssetIds
            val withBackground: Modifier.() -> Modifier = {
                if (isSelected) background(CoreColors.secondary.mainColor) else this
            }
            val withBorder: Modifier.() -> Modifier = {
                if (isSelected.not()) border(0.5.dp, Color.Black, shape) else this
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
                    .clickable { onClick(asset) }
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
private fun SmallAssetsSelection(
    detailUIItem: DetailUIItem.Asset,
    allowDelete: Boolean = true,
    modifier: Modifier = Modifier,
    onChange: (DetailUIItem.Asset) -> Unit
) {
    CoreSingleSmallAssetsSelection(
        selectedAsset = detailUIItem.value,
        possibleAssets = detailUIItem.possibleAssets,
        allowDelete = allowDelete,
        modifier = modifier.fillMaxSize().padding(15.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterVertically
        ),
    ) { text ->
        onChange(detailUIItem.copy(value = text))
    }
}

@Composable
fun CoreSingleSmallAssetsSelection(
    selectedAsset: Text?,
    possibleAssets: PersistentList<StaticTextId>,
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
        val value = if (asset.id != selectedAsset?.id?.raw || allowDelete.not()) {
            asset.toText(type = Text.Type.Default)
        } else {
            null
        }
        onChange(value)
    }
}

@Composable
fun CoreMultiSmallAssetsSelection(
    selectedAssets: PersistentList<Text>,
    possibleAssets: PersistentList<StaticTextId>,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    paddingForItem: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
    modifier: Modifier = Modifier,
    onChange: (assets: PersistentList<Text>) -> Unit
) {
    val selectedAssetIds = selectedAssets.map { it.id.raw }.toImmutableSet()

    CoreSmallAssetsSelection(
        selectedAssets = selectedAssets,
        possibleAssets = possibleAssets,
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        paddingForItem = paddingForItem
    ) { text ->
        val newAssets = if (text.id in selectedAssetIds) {
            selectedAssets.removeAll { it.id.raw == text.id }
        } else {
            val asset = text.toText(type = Text.Type.Default)
            selectedAssets.add(asset)
        }
        onChange(newAssets)
    }
}

enum class DetailTab(val staticId: StaticTextId) {
    City(StaticTextId.UiId.City),
    SexualOrientation(StaticTextId.UiId.SexualOrientation),
    Profession(StaticTextId.UiId.Profession),
    Height(StaticTextId.UiId.Height),
    Character(StaticTextId.UiId.Character),
    Language(StaticTextId.UiId.Language);

    companion object {
        private val mapper = entries.associateBy { it.staticId }

        fun parse(id: StaticTextId): DetailTab? = mapper[id]
    }
}

private const val DEFAULT_HEIGHT = 180
