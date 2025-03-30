package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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

@Composable
fun SwiperScreen(
    swiperScreenComponent: SwiperScreenComponent,
    navigateBar: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val state by swiperScreenComponent.state.subscribeAsState()

    CustomScaffold(
        topBar = { TopBar(sheetState) },
        bottomBar = navigateBar
    ) {
        if (TEST_MODE) {
            TestDeckOfCards()
        } else {
            DeckOfCards(swiperScreenComponent)
        }
    }

    ShearedPrefEditorDialog(
        sheetState = sheetState,
        searchPreferences = state.searchPreferences,
        applyDiff = { newSearchPref ->
            swiperScreenComponent.onEvent(ApplySearchPrefEvent(newSearchPref))
        }
    )
}

@Composable
private fun TopBar(
    sheetState: ModalBottomSheetState
) {
    val scope = rememberCoroutineScope()

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
            onClick = {
                scope.launch {
                    sheetState.show()
                }
            },
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
