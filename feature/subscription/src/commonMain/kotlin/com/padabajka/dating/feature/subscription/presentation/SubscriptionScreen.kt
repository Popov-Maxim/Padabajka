package com.padabajka.dating.feature.subscription.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.presentation.ui.layout.SimpleTopBar
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.Gradient
import com.padabajka.dating.core.presentation.ui.pager.PagerData
import com.padabajka.dating.core.presentation.ui.pager.PagerIndicators
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionEvent
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionInfo
import com.padabajka.dating.feature.subscription.presentation.model.SubscriptionScreenState

@Composable
fun SubscriptionScreen(component: SubscriptionScreenComponent) {
    val state by component.state.subscribeAsState()

    CustomScaffold(
        modifier = Modifier.background(
            brush = Brush.linearGradient(
                colors = Gradient.colorsForSubScreen,
                start = Gradient.rightTopOffset,
                end = Gradient.leftBottomOffset,
            ),
        ),
        topBar = {
            SimpleTopBar(
                title = StaticTextId.UiId.Subscription.translate(),
                navigateBack = {
                    component.onEvent(SubscriptionEvent.NavigateBack)
                }
            )
        },
    ) {
        Content(
            component = component,
            state = state,
        )
    }
}

@Composable
private fun Content(
    component: SubscriptionScreenComponent,
    state: SubscriptionScreenState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CardsBlock(
            modifier = Modifier.weight(1f)
        )
        PriceBlock(
            subInfo = state.monthSub,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Text(
                text = StaticTextId.UiId.SubscriptionFootnote.translate(),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier.padding(horizontal = 30.dp),
            ) {
                CoreCallToActionButton(
                    text = StaticTextId.UiId.Apply.translate(),
                    onClick = {
                        component.onEvent(SubscriptionEvent.Apply)
                    }
                )
            }
        }
    }
}

@Composable
private fun CardsBlock(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val subBenefits = listOf(
            SubBenefitUiItem(
                CoreIcons.Swiper.SuperLike.toData(),
                Color(color = 0xFF6C2D85),
                StaticTextId.UiId.BenefitSuperLikeTitle.translate(),
                StaticTextId.UiId.BenefitSuperLikeBody.translate()
            ),
            SubBenefitUiItem(
                CoreIcons.Swiper.Rewind.toData(),
                LocalContentColor.current,
                StaticTextId.UiId.BenefitReturnsTitle.translate(),
                StaticTextId.UiId.BenefitReturnsBody.translate()
            ),
            SubBenefitUiItem(
                CoreIcons.Swiper.Like.toData(),
                Color(color = 0xFF47C04C),
                StaticTextId.UiId.BenefitShowLikesTitle.translate(),
                StaticTextId.UiId.BenefitShowLikesBody.translate()
            )
        )
        var pagerData by remember { mutableStateOf(PagerData(0, subBenefits.size)) }

        SubBenefitCards(
            modifier = Modifier.weight(1f),
            subBenefits = subBenefits,
            onPageChanged = {
                pagerData = it
            }
        )
        PagerIndicators(
            modifier = Modifier.padding(10.dp),
            pagerData = pagerData,
            color = Color(color = 0xFF838383)
        )
    }
}

@Composable
private fun PriceBlock(
    subInfo: SubscriptionInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().heightIn(min = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically)
    ) {
        val shape = RoundedCornerShape(10.dp)
        Text(
            text = "${StaticTextId.UiId.Discount.translate()} ${subInfo.discount}%",
            color = CoreColors.primary.textColor,
            modifier = Modifier
                .background(CoreColors.primary.mainColor, shape)
                .padding(vertical = 5.dp, horizontal = 10.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val monthText = StaticTextId.UiId.Month.translate()
            Text(
                text = "${subInfo.oldPrice}/$monthText",
                fontSize = 16.sp,
                style = LocalTextStyle.current.copy(
                    textDecoration = TextDecoration.LineThrough
                )
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 32.sp
                        )
                    ) {
                        append(subInfo.price)
                    }
                    append("/")
                    append(monthText)
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CoreColors.secondary.mainColor
            )
        }
    }
}
