package com.padabajka.dating.core.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun AnimatedPopup(
    visible: Boolean,
    enterTransition: EnterTransition = defaultEnterTransition,
    exitTransition: ExitTransition = defaultExitTransition,
    offset: IntOffset = IntOffset(0, 0),
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    val transitionState = remember {
        MutableTransitionState(false).apply {
            targetState = false
        }
    }

    LaunchedEffect(visible) {
        transitionState.targetState = visible
    }

    if (transitionState.currentState || transitionState.targetState) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                clippingEnabled = true,
            ),
            offset = offset
        ) {
            AnimatedVisibility(
                visibleState = transitionState,
                enter = enterTransition,
                exit = exitTransition
            ) {
                content()
            }
        }
    }
}

private val defaultEnterTransition =
    fadeIn(animationSpec = tween(durationMillis = 200)) + scaleIn(initialScale = 0.8f)

private val defaultExitTransition =
    fadeOut(animationSpec = tween(durationMillis = 150)) + scaleOut(targetScale = 0.8f)
