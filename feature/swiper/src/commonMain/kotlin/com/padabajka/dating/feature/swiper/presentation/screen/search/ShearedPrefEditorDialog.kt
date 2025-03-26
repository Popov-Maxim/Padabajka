package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.padabajka.dating.feature.swiper.presentation.model.SearchPreferencesItem
import kotlinx.coroutines.flow.collectIndexed

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShearedPrefEditorDialog(
    sheetState: ModalBottomSheetState,
    searchPreferences: SearchPreferencesItem,
    applyDiff: (SearchPreferencesItem) -> Unit
) {
    var searchPreferencesItem by remember { mutableStateOf(searchPreferences) }
    val showReset = searchPreferencesItem != searchPreferences

    // Track sheet state changes
    LaunchedEffect(sheetState, searchPreferencesItem) {
        snapshotFlow { sheetState.currentValue }.collectIndexed { index, value ->
            if (index != 0 && value == ModalBottomSheetValue.Hidden) {
                applyDiff(searchPreferencesItem)
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            when (val searchPref = searchPreferencesItem) {
                SearchPreferencesItem.Loading -> {}
                is SearchPreferencesItem.Success -> SearchPrefEditor(
                    searchPreferences = searchPref,
                    showReset = showReset,
                    resetDiff = {
                        searchPreferencesItem = searchPreferences
                    }
                ) {
                    searchPreferencesItem = it
                }
            }
        }
    ) {}
}

@Composable
private fun SearchPrefEditor(
    searchPreferences: SearchPreferencesItem.Success,
    showReset: Boolean,
    resetDiff: () -> Unit,
    update: (SearchPreferencesItem.Success) -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.align(Alignment.CenterEnd).size(50.dp).padding(10.dp)) {
            if (showReset) {
                Icon(
                    modifier = Modifier.fillMaxSize().clickable(onClick = resetDiff),
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = "Done icon"
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AgeRange(ageRange = searchPreferences.ageRange) {
            update.invoke(
                searchPreferences.copy(
                    ageRange = it
                )
            )
        }
        GendersSelector(searchPreferences.lookingGenders) {
            update.invoke(
                searchPreferences.copy(
                    lookingGenders = it
                )
            )
        }
        DistanceSlider(searchPreferences.distanceInKm) {
            update.invoke(
                searchPreferences.copy(
                    distanceInKm = it
                )
            )
        }
    }
}
