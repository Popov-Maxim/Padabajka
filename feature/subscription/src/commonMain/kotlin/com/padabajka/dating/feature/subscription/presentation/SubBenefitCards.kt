package com.padabajka.dating.feature.subscription.presentation

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.IconData
import com.padabajka.dating.core.presentation.ui.pager.PagerData
import kotlinx.coroutines.delay

@Composable
fun SubBenefitCards(
    modifier: Modifier = Modifier,
    subBenefits: List<SubBenefitUiItem>,
    onPageChanged: (PagerData) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        subBenefits.size
    })
    LaunchedEffect(pagerState.currentPage) {
        val data = PagerData(pagerState.currentPage, pagerState.pageCount)
        onPageChanged(data)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(timeMillis = 2500L)

            val nextPage = (pagerState.currentPage + 1) % subBenefits.size
            pagerState.animateScrollToPage(
                nextPage,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutLinearInEasing
                )
            )
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
    ) { page ->
        BenefitCard(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            benefit = subBenefits[page]
        )
    }
}

@Composable
private fun BenefitCard(modifier: Modifier = Modifier, benefit: SubBenefitUiItem) {
    val shape = RoundedCornerShape(40.dp)
    Box(modifier = modifier.shadow(5.dp, shape).background(Color.White, shape)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(33.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val buttonModifier = Modifier.shadow(8.dp, CircleShape)
                .background(Color.White, CircleShape)

            Box(
                modifier = buttonModifier
                    .size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    iconData = benefit.icon,
                    tint = benefit.color
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = benefit.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = benefit.description,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class SubBenefitUiItem(
    val icon: IconData,
    val color: Color,
    val title: String,
    val description: String
)
