package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
import com.padabajka.dating.feature.swiper.presentation.model.ApplySearchPrefEvent
import com.padabajka.dating.feature.swiper.presentation.screen.search.ShearedPrefEditorDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwiperScreen(
    swiperScreenComponent: SwiperScreenComponent,
    navigateBar: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val state by swiperScreenComponent.state.subscribeAsState()

    CustomScaffold(
        topBar = {
            TopBar {
                scope.launch {
                    showBottomSheet = true
                }
            }
        },
        bottomBar = navigateBar
    ) {
        if (TEST_MODE) {
            TestDeckOfCards()
        } else {
            DeckOfCards(swiperScreenComponent)
        }
    }

    if (showBottomSheet) {
        ShearedPrefEditorDialog(
            sheetState = sheetState,
            searchPreferences = state.searchPreferences,
            applyDiff = { newSearchPref ->
                swiperScreenComponent.onEvent(ApplySearchPrefEvent(newSearchPref))
            },
            onDismissRequest = {
                showBottomSheet = false
            }
        )
    }
}

@Composable
private fun TopBar(
    openShearedPrefEditor: () -> Unit
) {
    Box(modifier = Modifier.height(70.dp).fillMaxWidth()) {
        Text(
            modifier = Modifier
                .padding(bottom = 7.dp)
                .align(Alignment.Center),
            text = "PADABAJKA",
            letterSpacing = 36.sp / 10,
            fontSize = 36.sp,
            fontFamily = PlayfairDisplay,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = openShearedPrefEditor,
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center).size(30.dp),
                painter = CoreIcons.SearchPref,
                contentDescription = "Settings",
            )
        }
    }
}

private const val TEST_MODE = false
