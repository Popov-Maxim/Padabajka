package com.padabajka.dating.feature.reaction.screen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.optionalClickable
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.presentation.ui.utils.rememberImageLoader
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.profile.presentation.ProfileViewBottomSheet
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import com.padabajka.dating.feature.reaction.screen.presentation.model.LikesMeEvent
import com.padabajka.dating.feature.reaction.screen.presentation.model.ListReactions
import com.padabajka.dating.feature.reaction.screen.presentation.model.ReactionUIState
import com.padabajka.dating.feature.reaction.screen.presentation.ui.GridSpan
import com.padabajka.dating.feature.reaction.screen.presentation.ui.LikesGrid

@Composable
fun LikesMeScreen(
    component: LikesMeScreenComponent,
    navigateBar: @Composable () -> Unit
) {
    val state by component.state.subscribeAsState()

    CustomScaffold(
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = StaticTextId.UiId.Likes.translate(),
                    fontFamily = PlayfairDisplay,
                    fontSize = 30.sp
                )
            }
        },
        bottomBar = navigateBar
    ) {
        when (val listReactions = state.listReactions) {
            ListReactions.Idle -> {}
            ListReactions.Loading -> {}
            is ListReactions.Error -> ErrorLikesMeScreen(listReactions)
            is ListReactions.Success -> SuccessLikesMeScreen(component, listReactions)
        }
    }
}

@Composable
private fun ErrorLikesMeScreen(state: ListReactions.Error) {
    Text(
        text = state.message
    )
}

@Composable
private fun SuccessLikesMeScreen(
    component: LikesMeScreenComponent,
    state: ListReactions.Success,
) {
    if (state.likes.isNotEmpty()) {
        HasLikesMeScreen(component, state)
    } else {
        EmptyLikesMeScreen()
    }
}

@Composable
private fun EmptyLikesMeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = StaticTextId.UiId.Likes.translate(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
                .padding(horizontal = 40.dp),
        )
    }
}

@Composable
private fun HasLikesMeScreen(
    component: LikesMeScreenComponent,
    state: ListReactions.Success,
) {
    val horizontalSpace = 10.dp
    val horizontalPadding = 10.dp

    var reactionUIState: ReactionUIState? by remember { mutableStateOf(null) }

    BoxWithConstraints {
        val width = maxWidth - horizontalSpace * 2 - horizontalPadding * 2
        val cardWidth = width / GridSpan.MAX

        LikesGrid(
            items = state.likes,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpace)
        ) { gridPlacedItem ->
            val modifier = Modifier.animateItem() // TODO(LikesMe): change animation
            val clickable = {
                reactionUIState = gridPlacedItem.state
            }
            when (gridPlacedItem.span) {
                GridSpan.ONE -> {
                    Box(modifier = modifier) {
                        ProfileImage(
                            image = gridPlacedItem.state.profile.images.first(),
                            modifier = Modifier.width(cardWidth).clickable(onClick = clickable)
                        )
                    }
                }

                GridSpan.TWO -> {
                    TwoGridItem(
                        state = gridPlacedItem.state as ReactionUIState.SuperLike,
                        cardWidth = cardWidth,
                        onClick = clickable,
                        modifier = modifier
                    )
                }

                GridSpan.FULL -> {
                    FullGridItem(
                        state = gridPlacedItem.state as ReactionUIState.SuperLike,
                        cardWidth = cardWidth,
                        onClick = clickable,
                        modifier = modifier
                    )
                }
            }
        }
    }

    reactionUIState?.let {
        ProfileViewBottomSheet(
            profileViewUIItem = it.profile,
            onDismissRequest = { reactionUIState = null },
            onLike = {
                component.onEvent(LikesMeEvent.Like(it.personId))
                reactionUIState = null
            },
            onDislike = {
                component.onEvent(LikesMeEvent.Dislike(it.personId))
                reactionUIState = null
            }
        )
    }
}

@Composable
private fun ProfileImage(
    image: Image,
    modifier: Modifier = Modifier,
) {
    val imageLoader = rememberImageLoader()

    Box(
        modifier = modifier.aspectRatio(ratio = 2.0f / 3)
            .clip(RoundedCornerShape(10.dp)).background(Color.DarkGray)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            imageLoader = imageLoader,
            model = image.raw(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun TwoGridItem(
    state: ReactionUIState.SuperLike,
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val profile = state.profile
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = gradient,
                start = Offset.Zero,
                end = Offset.Infinite.copy(x = 0f),
            ),
            shape = shape,
            alpha = 0.6f
        ).clip(shape).optionalClickable(onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            image = profile.images.first(),
            modifier = Modifier.width(cardWidth)
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfileHeader(profile)
        }
    }
}

@Composable
private fun FullGridItem(
    state: ReactionUIState.SuperLike,
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val profile = state.profile
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = gradient,
                start = Offset.Zero,
                end = Offset.Infinite.copy(x = 0f),
            ),
            shape = shape,
            alpha = 0.6f
        ).clip(shape).optionalClickable(onClick).padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            image = profile.images.first(),
            modifier = Modifier.width(cardWidth)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfileHeader(profile)

            val shape = RoundedCornerShape(15.dp).copy(bottomStart = CornerSize(0.dp))
            Column(
                modifier = Modifier.fillMaxWidth().background(Color.White, shape)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 2.dp,
                    ),
            ) {
                val message = state.message
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.height(IntrinsicSize.Max).padding(vertical = 4.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = message,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "12:45", // TODO: add message.sentTime.hourMinutes,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(profile: ProfileViewUIItem) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val nameWithAge = "${profile.name}, ${profile.age.raw}"
        Text(
            text = nameWithAge,
            fontSize = 15.sp,
            color = Color.White
        )

        Box(
            modifier = Modifier
                .background(
                    color = CoreColors.secondary.mainColor,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                text = profile.lookingFor.type.translate(),
                fontSize = 10.sp,
                lineHeight = 10.sp * 1.5,
                color = CoreColors.secondary.textColor
            )
        }
    }
}

private val gradient = listOf( // TODO: move to object Gradient
    Color(color = 0xff890F74),
    Color(color = 0xff4C2383)
)
