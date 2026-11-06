package com.padabajka.dating.core.presentation.ui.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import coil3.network.HttpException
import com.padabajka.dating.core.presentation.ui.image.CoreAsyncImage
import com.padabajka.dating.core.presentation.ui.image.raw
import com.padabajka.dating.core.repository.api.model.profile.Image
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch

@Composable
fun ImagePager(
    images: PersistentList<Image>,
    modifier: Modifier = Modifier,
    onPageChanged: (PagerData) -> Unit
) {
    var missingImages by remember(images) { mutableStateOf(emptySet<Image>()) }
    val availableImages = images.withIndex().filterNot { it.value in missingImages }
    val pagerState = rememberPagerState(pageCount = {
        availableImages.size
    })
    LaunchedEffect(pagerState.currentPage, availableImages.size) {
        val page = pagerState.currentPage.coerceIn(0, (availableImages.size - 1).coerceAtLeast(0))
        val data = PagerData(page, availableImages.size)
        onPageChanged(data)
    }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier.background(Color.DarkGray)) {
        if (availableImages.isEmpty()) return@Box

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = false,
            key = { page -> availableImages[page].index }
        ) { page ->
            val image = availableImages[page].value
            CoreAsyncImage(
                modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                model = image.raw(),
                onError = { error ->
                    if (error is HttpException && error.response.code == HTTP_NOT_FOUND) {
                        missingImages = missingImages + image
                    }
                }
            )
        }

        Row {
            Box(
                modifier = Modifier.fillMaxSize().weight(1f)
//                    .background(Color.Red.copy(alpha = 0.1f))
                    .clickableInvisible {
                        coroutineScope.launch {
                            pagerState.scrollBack()
                        }
                    }
            )

            Box(
                modifier = Modifier.fillMaxSize().weight(1f)
//                    .background(Color.Green.copy(alpha = 0.1f))
                    .clickableInvisible {
                        coroutineScope.launch {
                            pagerState.scrollToNext()
                        }
                    }
            )
        }
    }
}

private const val HTTP_NOT_FOUND = 404

private suspend fun PagerState.scrollBack() {
    if (this.currentPage > 0) {
        this.animateScrollToPage(this.currentPage - 1)
    }
}

private suspend fun PagerState.scrollToNext() {
    if (this.currentPage < this.pageCount - 1) {
        this.animateScrollToPage(this.currentPage + 1)
    }
}

@Composable
private fun Modifier.clickableInvisible(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier {
    return this.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = onClick,
        role = role,
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    )
}
