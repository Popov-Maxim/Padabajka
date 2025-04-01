package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.feature.swiper.presentation.model.SearchPreferencesItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectIndexed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShearedPrefEditorDialog(
    sheetState: SheetState,
    searchPreferences: SearchPreferencesItem,
    applyDiff: (SearchPreferencesItem) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var searchPreferencesItem by remember { mutableStateOf(searchPreferences) }
    val showReset = searchPreferencesItem != searchPreferences

    // Track sheet state changes
    LaunchedEffect(searchPreferences) {
        searchPreferencesItem = searchPreferences
    }
    LaunchedEffect(sheetState, searchPreferencesItem) {
        snapshotFlow { sheetState.currentValue }.collectIndexed { index, value ->
            if (index != 0 && value == SheetValue.Hidden) {
                applyDiff(searchPreferencesItem)
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        dragHandle = null // TODO add dragHandle
    ) {
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
}

@Composable
private fun SearchPrefEditor(
    searchPreferences: SearchPreferencesItem.Success,
    showReset: Boolean,
    resetDiff: () -> Unit,
    update: (SearchPreferencesItem.Success) -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Filters",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterStart).padding(20.dp)
        )

        Box(modifier = Modifier.padding(5.dp).align(Alignment.TopCenter)) {
            Spacer(
                modifier = Modifier.height(8.dp).width(70.dp)
                    .background(color = Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(5.dp))
            )
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(35.dp)) {
                if (showReset) {
                    Icon(
                        modifier = Modifier.fillMaxSize()
                            .clickable(onClick = resetDiff),
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Done icon"
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

//            Icon(
//                modifier = Modifier.size(24.dp),
//                imageVector = Icons.Filled.Info,
//                contentDescription = "Info icon"
//            )
//            Spacer(modifier = Modifier.width(20.dp))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GendersSelector(searchPreferences.lookingGenders.first()) {
            update.invoke(
                searchPreferences.copy(
                    lookingGenders = persistentListOf(it)
                )
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        AgeRange(ageRange = searchPreferences.ageRange) {
            update.invoke(
                searchPreferences.copy(
                    ageRange = it
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
