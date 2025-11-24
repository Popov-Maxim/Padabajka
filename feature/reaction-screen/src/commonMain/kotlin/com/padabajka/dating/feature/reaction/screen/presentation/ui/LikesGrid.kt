package com.padabajka.dating.feature.reaction.screen.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.padabajka.dating.feature.reaction.screen.presentation.layoutReactions
import com.padabajka.dating.feature.reaction.screen.presentation.model.ReactionUIState

@Composable
fun LikesGrid(
    items: List<ReactionUIState>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    item: @Composable LazyGridItemScope.(GridPlacedItem) -> Unit
) {
    val layout = remember(items) { layoutReactions(items) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(GridSpan.MAX),
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement
    ) {
        layout.forEach { placed ->
            item(
                span = { GridItemSpan(placed.span.span) }
            ) {
                item(placed)
            }
        }
    }
}
