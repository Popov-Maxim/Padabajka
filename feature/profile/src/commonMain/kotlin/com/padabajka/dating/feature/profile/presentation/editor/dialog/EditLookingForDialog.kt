package com.padabajka.dating.feature.profile.presentation.editor.dialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLookingForDialog(
    apply: (LookingForData) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = CoreColors.background.mainColor,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        dragHandle = null // TODO add dragHandle
    ) {
        DialogContent {
            apply(it)
            onDismissRequest()
        }
    }
}

@Composable
private fun DialogContent(
    apply: (LookingForData) -> Unit,
) {
    var screen: Screen by remember { mutableStateOf(Screen.Type) }

    AnimatedContent(
        modifier = Modifier.fillMaxHeight(fraction = 0.66f),
        targetState = screen,
        transitionSpec = {
            val k = if (this.targetState is Screen.Detail) 1 else -1
            slideInHorizontally { fullWidth -> k * fullWidth } + fadeIn() togetherWith
                slideOutHorizontally { fullWidth -> -k * fullWidth } + fadeOut()
        },
        label = "ScreenAnimation"
    ) { targetScreen ->
        CustomScaffold(
            topBar = {
                Box(modifier = Modifier.height(70.dp).fillMaxWidth()) {
                    when (targetScreen) {
                        Screen.Type -> {
                            Text(
                                text = StaticTextId.UiId.LookingFor.translate(),
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        is Screen.Detail -> {
                            Row(
                                modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                                    .padding(15.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = CoreIcons.BackArrow,
                                    contentDescription = "Back arrow",
                                    modifier = Modifier.clickable { screen = Screen.Type }
                                )
                                Text(
                                    text = targetScreen.type.translate(),
                                    fontSize = 24.sp
                                )
                            }
                        }
                    }
                }
            }
        ) {
            when (targetScreen) {
                Screen.Type -> TypeScreen {
                    screen = Screen.Detail(it)
                }

                is Screen.Detail -> DetailsScreen(lookingForAssets[targetScreen.type]!!) {
                    apply.invoke(createLookingForData(targetScreen.type, it))
                }
            }
        }
    }
}

@Composable
private fun TypeScreen(
    types: List<StaticTextId> = lookingForAssets.keys.toList(),
    onTypeClick: (StaticTextId) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        types.onEach { type ->
            DialogButton(
                text = type.translate(),
                description = null,
                onClick = { onTypeClick(type) }
            )
        }
    }
}

@Composable
private fun DetailsScreen(
    details: List<StaticTextId>,
    onDetailsClick: (StaticTextId) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        details.onEach { detail ->
            DialogButton(
                text = detail.translate(),
                description = null,
                onClick = { onDetailsClick(detail) }
            )
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    description: String?,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .shadow(5.dp, shape)
            .clip(shape)
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(100.dp)
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = text, fontSize = 15.sp)
                if (description != null) {
                    Text(text = description, fontSize = 12.sp)
                }
            }
            Icon(
                painter = CoreIcons.RightArrow,
                contentDescription = "Right arrow",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun createLookingForData(type: StaticTextId, detail: StaticTextId): LookingForData {
    val toText: StaticTextId.() -> Text = { Text(Text.Id(this.id)) }
    return LookingForData(type.toText(), detail.toText())
}

private sealed interface Screen {
    data object Type : Screen
    data class Detail(val type: StaticTextId) : Screen
}

private val lookingForAssets: Map<StaticTextId, List<StaticTextId>> = linkedMapOf(
    StaticTextId.AssetId.NonRomantic to listOf(
        StaticTextId.AssetId.LookingForFriends,
        StaticTextId.AssetId.PeopleToGoOutWith,
        StaticTextId.AssetId.LanguageExchange,
        StaticTextId.AssetId.Networking,
    ),

    StaticTextId.AssetId.CasualRelationship to listOf(
        StaticTextId.AssetId.Dating,
        StaticTextId.AssetId.Flirt,
        StaticTextId.AssetId.JustForFun,
    ),

    StaticTextId.AssetId.SeriousRelationship to listOf(
        StaticTextId.AssetId.SeriousRelationship,
        StaticTextId.AssetId.BuildingFamily,
    )
)
