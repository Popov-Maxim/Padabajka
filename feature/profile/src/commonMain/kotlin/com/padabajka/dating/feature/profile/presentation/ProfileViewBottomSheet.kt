package com.padabajka.dating.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreChipItem
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.GhostButton
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.pager.ImagePager
import com.padabajka.dating.core.presentation.ui.pager.PagerData
import com.padabajka.dating.core.presentation.ui.pager.PagerIndicators
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.ServerIcon
import com.padabajka.dating.feature.profile.presentation.editor.TotalDataBlock
import com.padabajka.dating.feature.profile.presentation.editor.asset.LanguageBlockView
import com.padabajka.dating.feature.profile.presentation.editor.asset.LifestyleBlockView
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesAssetsFields
import com.padabajka.dating.feature.profile.presentation.editor.model.LifestyleFields
import com.padabajka.dating.feature.profile.presentation.editor.model.isEmpty
import com.padabajka.dating.feature.profile.presentation.editor.model.toLanguagesFields
import com.padabajka.dating.feature.profile.presentation.editor.model.toLifestyleFields
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewMode
import com.padabajka.dating.feature.profile.presentation.model.ProfileViewUIItem
import kotlinx.collections.immutable.PersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewBottomSheet(
    profileViewUIItem: ProfileViewUIItem,
    onDismissRequest: () -> Unit,
    mode: ProfileViewMode = ProfileViewMode.None,
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
        ProfileViewContent(
            profileViewUIItem = profileViewUIItem,
            modifier = Modifier.fillMaxSize(),
            mode = mode,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
private fun BottomButtons(mode: ProfileViewMode, onDismissRequest: () -> Unit) {
    when (mode) {
        is ProfileViewMode.Discovery -> {
            ReactionsButtons(
                onLike = {
                    onDismissRequest()
                    mode.onLike()
                },
                onDislike = {
                    onDismissRequest()
                    mode.onDislike()
                }
            )
        }
        is ProfileViewMode.Match -> {
            MatchButtons(
                onDeleteChat = {
                    onDismissRequest()
                    mode.onDeleteChat()
                },
                onUnmatch = {
                    onDismissRequest()
                    mode.onUnmatch()
                }
            )
        }
        ProfileViewMode.None -> Unit
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
            .shadow(elevation = 5.dp, shape = CircleShape)

        IconButton(
            modifier = buttonModifier
                .size(64.dp).weight(1f),
            onClick = { onDislike() },
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = Color.White,
            )
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
            onClick = { onLike() },
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = Color(color = 0xFF47C04C),
                contentColor = Color.White
            )
        ) {
            Icon(
                modifier = Modifier.size(34.dp),
                imageVector = Icons.Default.Favorite,
                contentDescription = "Like",
            )
        }
    }
}

@Composable
private fun MatchButtons(
    modifier: Modifier = Modifier,
    onDeleteChat: () -> Unit = {},
    onUnmatch: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        GhostButton(
            text = "Удалить чат",
            onClick = onDeleteChat
        )
        GhostButton(
            text = "Удалить метч",
            onClick = onUnmatch
        )
    }
}

@Composable
private fun ProfileViewContent(
    profileViewUIItem: ProfileViewUIItem,
    modifier: Modifier = Modifier,
    mode: ProfileViewMode,
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        BlockWithImagePager(profileViewUIItem)
        Column(
            modifier = Modifier.padding(vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(70.dp)
        ) {
            TextAssetsInProfile(
                modifier = Modifier.padding(horizontal = 20.dp),
                profileViewUIItem = profileViewUIItem
            )

            BottomButtons(mode, onDismissRequest)
        }
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

        val gradientBottom = Brush.verticalGradient(
            colors = gradientBottomAlfa.map { Color.Black.copy(alpha = it) }
        )
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
                .fillMaxWidth().height(84.dp)
                .background(gradientBottom)
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            val textColor = Color.White
            Text(
                text = "${profileViewUIItem.name}, ${profileViewUIItem.age.raw}",
                fontSize = 26.sp,
                color = textColor,
                modifier = Modifier.align(Alignment.BottomStart)
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
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        AboutMe(
            text = profileViewUIItem.aboutMe
        )
        Details(
            details = profileViewUIItem.details
        )
        Lifestyle(
            field = profileViewUIItem.lifestyle.toLifestyleFields()
        )
        Language(
            field = profileViewUIItem.languages.toLanguagesFields()
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
    if (details.isNotEmpty()) {
        AssetBlock(
            modifier = modifier,
            label = StaticTextId.UiId.BasicInfo
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
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
}

@Composable
private fun Lifestyle(
    field: LifestyleFields,
    modifier: Modifier = Modifier
) {
    if (field.supportedLifestyles.isEmpty().not()) {
        AssetBlock(
            label = StaticTextId.UiId.Lifestyle,
            modifier = modifier
        ) {
            LifestyleBlockView(field)
        }
    }
}

@Composable
private fun Language(
    field: LanguagesAssetsFields,
    modifier: Modifier = Modifier
) {
    if (field.isEmpty().not()) {
        AssetBlock(
            label = StaticTextId.UiId.Language,
            modifier = modifier
        ) {
            LanguageBlockView(field)
        }
    }
}

@Composable
private fun AssetBlock(
    label: StaticTextId.UiId,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    TotalDataBlock(
        label = label.translate(),
        modifier = modifier,
        content = content
    )
}

@Composable
@Suppress("UnusedParameter")
private fun ChipItem(
    text: String,
    icon: ServerIcon?,
    modifier: Modifier = Modifier
) {
    CoreChipItem(
        text = text,
        icon = null,
        modifier = modifier
    )
}

@Suppress("MagicNumber")
private val gradientTopAlfa = listOf(0f, 0.3f, 0.8f).reversed()

@Suppress("MagicNumber")
private val gradientBottomAlfa = listOf(0f, 0.6f, 1f)

private const val RATIO_FOR_IMAGE = 3f / 4
