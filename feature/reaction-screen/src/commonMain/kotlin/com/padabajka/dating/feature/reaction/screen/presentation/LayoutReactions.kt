package com.padabajka.dating.feature.reaction.screen.presentation

import com.padabajka.dating.feature.reaction.screen.presentation.model.ReactionUIState
import com.padabajka.dating.feature.reaction.screen.presentation.ui.GridPlacedItem
import com.padabajka.dating.feature.reaction.screen.presentation.ui.GridSpan

fun layoutReactions(items: List<ReactionUIState>): List<GridPlacedItem> {
    val result = mutableListOf<GridPlacedItem>()
    var i = 0

    while (i < items.size) {
        val item = items[i]

        result += when (item) {
            is ReactionUIState.SuperLike -> {
                val next = items.getOrNull(i + 1)
                if (item.message != "" || next !is ReactionUIState.Like) {
                    GridPlacedItem(item, GridSpan.FULL)
                } else {
                    GridPlacedItem(item, GridSpan.TWO)
                }
            }

            is ReactionUIState.Like -> {
                GridPlacedItem(item, GridSpan.ONE)
            }
        }
        i++
    }

    return result
}
