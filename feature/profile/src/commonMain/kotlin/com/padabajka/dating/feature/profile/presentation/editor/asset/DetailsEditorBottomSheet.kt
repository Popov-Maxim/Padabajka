package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.core.presentation.ui.utils.rememberUpdatedMutableState
import com.padabajka.dating.feature.profile.presentation.editor.asset.picker.TickWheelPicker
import com.padabajka.dating.feature.profile.presentation.editor.model.CitySearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUIItem
import com.padabajka.dating.feature.profile.presentation.editor.model.FoundedAssets
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedDetails
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateCitySearchEvent

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
        Column(
            modifier = Modifier.fillMaxHeight(fraction = 2f / 3).padding(top = 25.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            NavigateTabs(tabsState, onTabSelected)
            Content(
                tabsState = tabsState,
                supportedDetails = supportedDetails,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun NavigateTabs(tabsState: DetailTab, changeState: (DetailTab) -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .horizontalScroll(scrollState).fillMaxWidth()
            .background(
                color = Color(color = 0xFFD9D9D9),
                shape = shape
            )
    ) {
        DetailTab.entries.onEach { tab ->
            val isSelected = tab == tabsState
            val withBackground: Modifier.() -> Modifier = {
                if (isSelected) background(CoreColors.secondary.mainColor) else this
            }
            val contentColor = if (isSelected) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .clip(shape)
                    .clickable { changeState(tab) }
                    .withBackground()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
    onEvent: (ProfileEditorEvent) -> Unit,
) {
    when (tabsState) {
        DetailTab.Height -> Height(supportedDetails.height)
        DetailTab.City -> City(supportedDetails.city, onEvent)
        else -> {}
    }
}

@Composable
private fun Height(
    heightItem: DetailUIItem.Int,
    modifier: Modifier = Modifier,
) {
    var height: Int? by rememberUpdatedMutableState(heightItem.value ?: DEFAULT_HEIGHT)
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
                selectedValue = height ?: 140,
                onValueChange = { height = it }
            )
            TextEditField(
                modifier = Modifier.width(100.dp),
                text = height?.toString() ?: "",
                onChange = { height = it.toIntOrNull() },
            )
        }
    }
}

@Composable
private fun City(
    cityItem: DetailUIItem.AssetFromDb,
    onEvent: (ProfileEditorEvent) -> Unit,
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
        TextEditField(
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
                CitiesList(foundedAssets)
            }
        }
    }
}

@Composable
private fun CitiesList(foundedAssets: FoundedAssets.Success) {
    LazyColumn(modifier = Modifier.fillMaxSize().innerShadow()) {
        val possibleAssets = foundedAssets.possibleAssets
        itemsIndexed(possibleAssets) { index, city ->
            Box(
                modifier = Modifier.clickable { }
                    .fillMaxWidth().padding(horizontal = 20.dp, vertical = 15.dp)
            ) {
                Text(
                    text = city,
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

enum class DetailTab(val staticId: StaticTextId) {
    City(StaticTextId.UiId.City),
    SexualOrientation(StaticTextId.UiId.SexualOrientation),
    Education(StaticTextId.UiId.Education),
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
