package com.padabajka.dating.core.presentation.ui.pager

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.mainColor

@Composable
fun PagerIndicators(modifier: Modifier, pagerData: PagerData) {
    Row(modifier = modifier) {
        pagerData.getPageTypes(RADIUS).onEach {
            AnimatedPageIndicator(it)
            Spacer(Modifier.width(2.dp))
        }
    }
}

private fun PagerData.getPageTypes(radius: Int): List<PageType> {
    return List(size) {
        if (it in (page - radius)..(page + radius)) {
            if (it == page) PageType.Selected else PageType.Unselected
        } else {
            PageType.Fars
        }
    }
}

@Suppress("UnusedPrivateMember")
private fun PagerData.getPageTypesV2(radius: Int): List<PageType> {
    val lastIndex = size - 1
    val startIndex = page - radius
    val endIndex = minOf(lastIndex, page + radius)
    return buildList {
        if (startIndex > 0) add(PageType.Fars)
        val countLeftSelected = minOf(radius, page)
        repeat(countLeftSelected) {
            add(PageType.Unselected)
        }
        add(PageType.Selected)
        val countRightSelected = minOf(radius, lastIndex - page)
        repeat(countRightSelected) {
            add(PageType.Unselected)
        }
        if (endIndex < lastIndex) add(PageType.Fars)
    }
}

private enum class PageType {
    Selected,
    Unselected,
    Fars
}

@Composable
private fun AnimatedPageIndicator(pageType: PageType) {
    val targetWidth = when (pageType) {
        PageType.Selected -> 50.dp
        PageType.Unselected -> 15.dp
        PageType.Fars -> 5.dp
    }

    val animatedWidth by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    val targetColor = when (pageType) {
        PageType.Selected -> CoreColors.background.mainColor
        PageType.Unselected,
        PageType.Fars -> CoreColors.background.mainColor.copy(alpha = 0.5f)
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    IndicatorTemplate(animatedWidth, animatedColor)
}

@Composable
private fun IndicatorTemplate(width: Dp, color: Color) {
    Box(
        modifier = Modifier
            .size(width, 5.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(5.dp)
            )
    )
}

private const val RADIUS = 3
