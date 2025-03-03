package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.profile.raw
import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import com.fp.padabajka.feature.swiper.presentation.model.EmptyCardItem
import com.fp.padabajka.feature.swiper.presentation.model.LoadingItem
import com.fp.padabajka.feature.swiper.presentation.model.NativeAdItem
import com.fp.padabajka.feature.swiper.presentation.model.PersonItem
import kotlinx.collections.immutable.PersistentList
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun Card(cardItem: CardItem) {
    when (cardItem) {
        EmptyCardItem -> {}
        LoadingItem -> {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                )
            }
        }

        is NativeAdItem -> {
            NativeAdCard(cardItem)
        }

        is PersonItem -> PersonCard(cardItem)
    }
}

@Composable
private fun PersonCard(personItem: PersonItem) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Gray)
            .border(1.dp, Color.Black)
    ) {
        ImagePager(
            modifier = Modifier.height(400.dp).fillMaxWidth(),
            images = personItem.images
        )

        Text(
            text = "firstName: ${personItem.firstName}",
            textAlign = TextAlign.Center,
        )
        Text(
            text = "lastName: ${personItem.lastName}",
            textAlign = TextAlign.Center,
        )
        Text(
            text = "lastName: ${personItem.birthday}",
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImagePager(
    modifier: Modifier,
    images: PersistentList<Image>,
) {
    val context = LocalPlatformContext.current
    val imageLoader: ImageLoader = koinInject { parametersOf(context) }
    val pagerState = rememberPagerState(pageCount = {
        images.size
    })
    HorizontalPager(
        modifier = modifier,
        state = pagerState
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
}
