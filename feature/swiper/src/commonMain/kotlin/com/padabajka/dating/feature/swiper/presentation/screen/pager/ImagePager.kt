package com.padabajka.dating.feature.swiper.presentation.screen.pager

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import coil3.compose.AsyncImage
import com.padabajka.dating.core.presentation.ui.utils.rememberImageLoader
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.raw
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch

@Composable
fun ImagePager(
    modifier: Modifier,
    images: PersistentList<Image>,
    onPageChanged: (PagerData) -> Unit
) {
    val imageLoader = rememberImageLoader()
    val pagerState = rememberPagerState(pageCount = {
        images.size
    })
    LaunchedEffect(pagerState.currentPage) {
        val data = PagerData(pagerState.currentPage, pagerState.pageCount)
        onPageChanged(data)
    }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier) {
        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            val image = images[page]
            AsyncImage(
                modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                imageLoader = imageLoader,
                model = image.raw(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Row {
            Box(
                modifier = Modifier.fillMaxSize().weight(1f)
//                    .background(Color.Red.copy(alpha = 0.1f))
                    .clickableInvisiable {
                        coroutineScope.launch {
                            pagerState.scrollBack()
                        }
                    }
            )

            Box(
                modifier = Modifier.fillMaxSize().weight(1f)
//                    .background(Color.Green.copy(alpha = 0.1f))
                    .clickableInvisiable {
                        coroutineScope.launch {
                            pagerState.scrollToNext()
                        }
                    }
            )
        }
    }
}

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
private fun Modifier.clickableInvisiable(
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
