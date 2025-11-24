package com.padabajka.dating.feature.reaction.screen.presentation.ui

import com.padabajka.dating.feature.reaction.screen.presentation.model.ReactionUIState

data class GridPlacedItem(
    val state: ReactionUIState,
    val span: GridSpan
)

enum class GridSpan(val span: Int) {
    ONE(span = 1),
    TWO(span = 2),
    FULL(span = 3);

    companion object {
        const val MAX = 3
    }
}
