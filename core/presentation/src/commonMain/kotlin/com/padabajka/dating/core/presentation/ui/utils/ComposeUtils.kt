package com.padabajka.dating.core.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun <T> rememberUpdatedMutableState(newValue: T): MutableState<T> = remember {
    mutableStateOf(newValue)
}.apply {
    LaunchedEffect(newValue) {
        value = newValue
    }
}
