package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    useIme: Boolean = true,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(modifier.optionalImePadding(useIme)) {
        topBar()
        MainBlock(content)
        bottomBar()
    }
}

@Composable
private inline fun ColumnScope.MainBlock(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.weight(1f),
    ) {
        content()
    }
}

private fun Modifier.optionalImePadding(use: Boolean): Modifier = if (use) imePadding() else this
