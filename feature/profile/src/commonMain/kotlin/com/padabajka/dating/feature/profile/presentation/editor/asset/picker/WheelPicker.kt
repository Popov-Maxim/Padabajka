package com.padabajka.dating.feature.profile.presentation.editor.asset.picker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun TickWheelPicker(
    modifier: Modifier = Modifier,
    range: IntRange = heightRange,
    selectedValue: Int,
    onValueChange: (Int) -> Unit
) {
    val correctSelectedValue by rememberUpdatedState(selectedValue.coerceIn(range))
    val itemHeight = 32.dp
    val density = LocalDensity.current
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    var containerHeightPx by remember { mutableStateOf(0) }
    val contentPaddingPx = remember(containerHeightPx) {
        (containerHeightPx / 2 - itemHeightPx / 2).coerceAtLeast(0f)
    }
    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = correctSelectedValue - range.first)
    LaunchedEffect(correctSelectedValue) {
        val newValue = countNewSelectValue(listState, range)
        if (newValue != correctSelectedValue) {
            listState.animateScrollToItem(correctSelectedValue - range.first)
        }
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val newValue = countNewSelectValue(listState, range)
                if (newValue != correctSelectedValue) {
                    onValueChange(newValue)
                }
                return super.onPostScroll(consumed, available, source)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val newValue = countNewSelectValue(listState, range)
                listState.animateScrollToItem(newValue - range.first)

                return super.onPostFling(consumed, available)
            }
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TriangleArrow(Direction.Right)
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().weight(2f)
                .onGloballyPositioned {
                    containerHeightPx = it.size.height
                }.nestedScroll(nestedScrollConnection),
            contentPadding = PaddingValues(
                top = with(density) { contentPaddingPx.toDp() },
                bottom = with(density) { contentPaddingPx.toDp() }
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(range.count()) { index ->
                val layoutInfo = listState.layoutInfo
                val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                val centeredItem = layoutInfo.visibleItemsInfo.minByOrNull { item ->
                    val itemCenter = item.offset + item.size / 2
                    abs(itemCenter - center)
                }
                val centerIndex = centeredItem?.index ?: 0

                val distanceFromCenter = abs(index - centerIndex)

                val animationScale by animateFloatAsState(
                    targetValue = 1f - ((distanceFromCenter * distanceFromCenter * itemHeight.value / 32 / 7) * 0.1f)
                        .coerceAtMost(maximumValue = 0.8f)
                )
                val alpha by animateFloatAsState(
                    targetValue = 1f - (distanceFromCenter * 0.1f)
                        .coerceAtMost(maximumValue = 0.7f)
                )

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            this.alpha = alpha
                            this.scaleX = animationScale
                            this.scaleY = animationScale
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(Color.Black)
                    )
                }
            }
        }
        TriangleArrow(Direction.Left)
    }
}

private fun countNewSelectValue(listState: LazyListState, range: IntRange): Int {
    val layoutInfo = listState.layoutInfo
    val center = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    val centeredItem = layoutInfo.visibleItemsInfo.minByOrNull { item ->
        val itemCenter = item.offset + item.size / 2
        abs(itemCenter - center)
    }

    return range.first + (centeredItem?.index ?: listState.firstVisibleItemIndex)
}

private const val MIN_HEIGHT = 140
private const val MAX_HEIGHT = 300
private val heightRange = MIN_HEIGHT..MAX_HEIGHT
