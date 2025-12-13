package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.pager.ImagePager
import com.padabajka.dating.core.presentation.ui.pager.PagerData
import com.padabajka.dating.core.presentation.ui.pager.PagerIndicators
import com.padabajka.dating.feature.profile.presentation.ProfileViewBottomSheet
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewMode
import com.padabajka.dating.feature.swiper.presentation.model.CardItem
import com.padabajka.dating.feature.swiper.presentation.model.EmptyCardItem
import com.padabajka.dating.feature.swiper.presentation.model.LoadingItem
import com.padabajka.dating.feature.swiper.presentation.model.NativeAdItem
import com.padabajka.dating.feature.swiper.presentation.model.PersonItem
import com.padabajka.dating.feature.swiper.presentation.model.toPersonView
import com.padabajka.dating.feature.swiper.presentation.screen.card.CardReaction

@Composable
fun Card(
    modifier: Modifier = Modifier,
    cardItem: CardItem,
    onReaction: (CardReaction) -> Unit
) {
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

        is PersonItem -> PersonCard(modifier, cardItem, onReaction)
    }
}

@Composable
private fun PersonCard(
    modifier: Modifier,
    personItem: PersonItem,
    onReaction: (CardReaction) -> Unit
) {
    Box(
        modifier
    ) {
        var pagerData by remember { mutableStateOf(PagerData(0, personItem.images.size)) }
        ImagePager(
            modifier = Modifier.fillMaxSize(),
            images = personItem.images
        ) {
            pagerData = it
        }
        val gradientTop = Brush.verticalGradient(
            colors = gradientTopAlfa.map { Color.Black.copy(alpha = it) },
        )
        Box(
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().height(60.dp)
                .background(gradientTop)
        ) {
            PagerIndicators(
                modifier = Modifier.align(Alignment.TopCenter).padding(10.dp),
                pagerData = pagerData
            )
        }

        val gradientBottom = Brush.verticalGradient(
            colors = gradientBottomAlfa.map { Color.Black.copy(alpha = it) }
        )
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .background(gradientBottom)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                val textColor = Color.White
                Text(
                    text = "${personItem.name}, ${personItem.age.raw}",
                    fontSize = 26.sp,
                    color = textColor
                )
                var showViewProfile by remember { mutableStateOf(false) }
                Text(
                    text = personItem.aboutMe,
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier.clickable {
                        showViewProfile = true
                    }
                )
                if (showViewProfile) {
                    ProfileViewBottomSheet(
                        profileViewUIItem = personItem.toPersonView(),
                        onDismissRequest = { showViewProfile = false },
                        mode = ProfileViewMode.Discovery(
                            onLike = {
                                onReaction(CardReaction.Like)
                            },
                            onDislike = {
                                onReaction(CardReaction.Dislike)
                            }
                        )
                    )
                }
                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val buttonModifier = Modifier.align(Alignment.CenterVertically)
                        .background(CoreColors.background.mainColor, CircleShape)

                    IconButton(
                        modifier = buttonModifier
                            .size(48.dp),
                        onClick = {
                            onReaction(CardReaction.Return)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(34.dp),
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Return",
                        )
                    }

                    IconButton(
                        modifier = buttonModifier
                            .size(64.dp),
                        onClick = { onReaction(CardReaction.Dislike) }
                    ) {
                        Icon(
                            modifier = Modifier.size(34.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dislike",
                        )
                    }

                    IconButton(
                        modifier = buttonModifier
                            .size(64.dp),
                        onClick = { onReaction(CardReaction.Like) }
                    ) {
                        Icon(
                            modifier = Modifier.size(34.dp),
                            imageVector = Icons.Default.Favorite,
                            tint = Color(color = 0xFF47C04C),
                            contentDescription = "Like",
                        )
                    }

                    IconButton(
                        modifier = buttonModifier
                            .size(48.dp),
                        onClick = {
                            onReaction(CardReaction.SuperLike)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(34.dp),
                            imageVector = Icons.Default.Star,
                            tint = Color(color = 0xFF6C2D85),
                            contentDescription = "SuperLike",
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Suppress("MagicNumber")
private val gradientTopAlfa = listOf(0f, 0.3f, 0.8f).reversed()

@Suppress("MagicNumber")
private val gradientBottomAlfa = listOf(0f, 0.6f, 1f)
