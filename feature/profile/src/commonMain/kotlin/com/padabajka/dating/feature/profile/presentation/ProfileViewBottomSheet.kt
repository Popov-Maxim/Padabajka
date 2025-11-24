package com.padabajka.dating.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.pager.ImagePager
import com.padabajka.dating.core.presentation.ui.pager.PagerData
import com.padabajka.dating.core.presentation.ui.pager.PagerIndicators
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.ServerIcon
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import kotlinx.collections.immutable.PersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewBottomSheet(
    profileViewUIItem: ProfileViewUIItem,
    onDismissRequest: () -> Unit,
    onLike: () -> Unit = {},
    onDislike: () -> Unit = {},
) {
    val bottomSheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        modifier = Modifier.padding(top = 50.dp),
        sheetState = bottomSheetState,
        containerColor = CoreColors.background.mainColor,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        Box {
            ProfileViewContent(
                profileViewUIItem,
                Modifier.fillMaxSize()
//                .padding(
//                    WindowInsets.systemBars
//                        .asPaddingValues()
//                )
//                .background(
//                    CoreColors.background.mainColor,
//                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
//                )
            )

            // TODO: fix ui
            ReactionsButtons(Modifier.align(Alignment.BottomCenter), onLike, onDislike)
        }
    }
}

@Composable
private fun ReactionsButtons(
    modifier: Modifier = Modifier,
    onLike: () -> Unit = {},
    onDislike: () -> Unit = {},
) {
    Row(
        modifier = modifier.padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val buttonModifier = Modifier.align(Alignment.CenterVertically)
            .background(CoreColors.background.mainColor, CircleShape)
            .border(width = 1.dp, color = Color.Black)

        IconButton(
            modifier = buttonModifier
                .size(64.dp).weight(1f),
            onClick = { onDislike() }
        ) {
            Icon(
                modifier = Modifier.size(34.dp),
                imageVector = Icons.Default.Close,
                contentDescription = "Dislike",
            )
        }

        IconButton(
            modifier = buttonModifier
                .height(64.dp).weight(2f),
            onClick = { onLike() }
        ) {
            Icon(
                modifier = Modifier.size(34.dp),
                imageVector = Icons.Default.Favorite,
                tint = Color(color = 0xFF47C04C),
                contentDescription = "Like",
            )
        }
    }
}

@Composable
private fun ProfileViewContent(
    profileViewUIItem: ProfileViewUIItem,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(modifier.verticalScroll(scrollState)) {
        BlockWithImagePager(profileViewUIItem)
        TextAssetsInProfile(profileViewUIItem)
    }
}

@Composable
private fun BlockWithImagePager(profileViewUIItem: ProfileViewUIItem) {
    val images = profileViewUIItem.images
    var pagerData by remember { mutableStateOf(PagerData(0, images.size)) }
    Box {
        ImagePager(
            modifier = Modifier.fillMaxWidth().aspectRatio(RATIO_FOR_IMAGE),
            images = images
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
    }
}

@Composable
private fun TextAssetsInProfile(
    profileViewUIItem: ProfileViewUIItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 40.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        AboutMe(
            text = profileViewUIItem.aboutMe
        )
        Details(
            details = profileViewUIItem.details
        )
    }
}

@Composable
private fun AboutMe(
    text: String,
    modifier: Modifier = Modifier
) {
    AssetBlock(
        modifier = modifier,
        label = StaticTextId.UiId.Bio
    ) {
        Text(
            text = text,
            fontSize = 15.sp
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Details(
    details: PersistentList<Detail>,
    modifier: Modifier = Modifier
) {
    AssetBlock(
        modifier = modifier,
        label = StaticTextId.UiId.BasicInfo
    ) {
        FlowRow(
//            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            details.onEach { detail ->
                val text = when (val value = detail.value) {
                    is Detail.Value.Centimeter -> value.raw.toString()
                    is Detail.Value.String -> value.raw
                    is Detail.Value.Asset -> value.raw.translate()
                }
                ChipItem(
                    text = text,
                    icon = detail.icon,
                )
            }
        }
    }
}

@Composable
private fun AssetBlock(
    label: StaticTextId.UiId,
    modifier: Modifier,
    context: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label.translate(),
            fontSize = 24.sp
        )
        context()
    }
}

@Composable
private fun ChipItem(
    text: String,
    icon: ServerIcon?,
    modifier: Modifier = Modifier
) {
    AssistChip(
        label = {
            Text(text = text, fontSize = 16.sp)
        },
        modifier = modifier,
        leadingIcon = { LeadingIcon(icon) },
        onClick = {},
//        enabled = false,
        shape = RoundedCornerShape(20.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = CoreColors.secondary.mainColor,
            labelColor = CoreColors.secondary.textColor,
            leadingIconContentColor = CoreColors.secondary.textColor
        ),
//        elevation = TODO(),
        border = null,
//        interactionSource = TODO()
    )
}

@Composable
@Suppress("UnusedParameter")
private fun LeadingIcon(icon: ServerIcon?) {
    // TODO
}

@Suppress("MagicNumber")
private val gradientTopAlfa = listOf(0f, 0.3f, 0.8f).reversed()

private const val RATIO_FOR_IMAGE = 3f / 4
