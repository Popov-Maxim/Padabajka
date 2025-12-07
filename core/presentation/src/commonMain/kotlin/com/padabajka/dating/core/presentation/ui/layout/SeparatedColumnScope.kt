package com.padabajka.dating.core.presentation.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

class SeparatedColumnScope {
    internal val items = mutableListOf<@Composable () -> Unit>()

    fun item(content: @Composable () -> Unit) {
        items += content
    }
}

@Composable
fun SeparatedColumn(
    modifier: Modifier = Modifier,
    divider: @Composable () -> Unit,
    content: SeparatedColumnScope.() -> Unit
) {
    val scope = remember { SeparatedColumnScope() }

    scope.content()

    Column(modifier) {
        scope.items.forEachIndexed { index, item ->
            key(index) { item() }
            if (index < scope.items.lastIndex) divider()
        }
    }
}
