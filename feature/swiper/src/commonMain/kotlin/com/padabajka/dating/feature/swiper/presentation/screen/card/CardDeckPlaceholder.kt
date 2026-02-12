package com.padabajka.dating.feature.swiper.presentation.screen.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreCircularProgressIndicator
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.feature.swiper.presentation.model.CardDeckState
import com.padabajka.dating.feature.swiper.presentation.model.ResetSearchPrefEventToDefault
import com.padabajka.dating.feature.swiper.presentation.model.SearchPreferencesItem
import com.padabajka.dating.feature.swiper.presentation.model.SwiperEvent
import com.padabajka.dating.feature.swiper.presentation.model.SwiperState
import com.padabajka.dating.feature.swiper.presentation.model.UnfreezeProfileEvent

@Composable
fun CardDeckPlaceholder(
    state: SwiperState,
    modifier: Modifier = Modifier,
    onEvent: (SwiperEvent) -> Unit
) {
    val cardDeckState = state.cardDeckState
    Box(modifier) {
        when (cardDeckState) {
            CardDeckState.Empty -> Empty(state, onEvent)
            CardDeckState.Error -> Error(
                modifier = Modifier.align(Alignment.Center)
            )

            CardDeckState.Frozen -> Frozen(onEvent)
            CardDeckState.Idle -> {}
            CardDeckState.Loading -> Loading(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun Frozen(
    onEvent: (SwiperEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp)
    ) {
        SimpleText(
            text = StaticTextId.UiId.CardDeckFrozenProfile.translate(),
            modifier = Modifier.weight(1f)
        )
        CoreCallToActionButton(
            text = StaticTextId.UiId.Unfreeze.translate(),
            onClick = { onEvent.invoke(UnfreezeProfileEvent) }
        )
    }
}

@Composable
private fun Empty(
    state: SwiperState,
    onEvent: (SwiperEvent) -> Unit
) {
    val searchPreferences = state.searchPreferences
    val enabledResetButton = when (searchPreferences) {
        SearchPreferencesItem.Loading -> false
        is SearchPreferencesItem.Success -> searchPreferences.isDefault.not()
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp)
    ) {
        SimpleText(
            text = StaticTextId.UiId.CardDeckEmptyProfiles.translate(),
            modifier = Modifier.weight(1f)
        )
        CoreCallToActionButton(
            text = StaticTextId.UiId.ResetSearchFilters.translate(),
            enabled = enabledResetButton,
            onClick = { onEvent.invoke(ResetSearchPrefEventToDefault) }
        )
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LocalText(
            text = StaticTextId.UiId.CardDeckErrorLoadingProfiles.translate(),
        )
        Icon(
            modifier = Modifier.size(44.dp),
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
        )
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LocalText(
            text = StaticTextId.UiId.Loading.translate(),
        )
        CoreCircularProgressIndicator()
    }
}

@Composable
private fun SimpleText(text: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        LocalText(
            text,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun LocalText(text: String, modifier: Modifier = Modifier) {
    val titleFontSize = 20.sp
    Text(
        text = text,
        modifier = modifier,
        fontSize = titleFontSize,
        lineHeight = titleFontSize * 1.3,
        letterSpacing = titleFontSize * 0.1,
        textAlign = TextAlign.Center,
        fontFamily = PlayfairDisplay
    )
}
