package com.padabajka.dating.feature.profile.presentation.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.TextInputField
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.image.rememberImagePicker
import com.padabajka.dating.feature.profile.presentation.editor.dialog.EditImageDialog
import com.padabajka.dating.feature.profile.presentation.editor.model.AboutMeFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DeleteImageEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DiscardProfileUpdatesClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ImageAddEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.NavigateBackEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SaveProfileUpdatesClickEvent
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileEditorScreen(component: ProfileEditorScreenComponent) {
    val state by component.state.subscribeAsState()
    CustomScaffold(
        topBar = { TopBar(component::onEvent) }
    ) {
        Column {
            Box(modifier = Modifier.padding(10.dp)) {
                ImageFields(images = state.images.value, component = component)
            }

            val aboutMe = state.aboutMe.value
            TextEditField(
                modifier = Modifier.padding(20.dp),
                text = aboutMe,
                label = StaticTextId.UiId.Bio.translate(),
                onChange = {
                    component.onEvent(AboutMeFieldUpdateEvent(it))
                },
            )

            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        component.onEvent(DiscardProfileUpdatesClickEvent)
                    }
                ) {
                    Text(
                        text = "Сбросить"
                    )
                }

                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        component.onEvent(SaveProfileUpdatesClickEvent)
                    }
                ) {
                    Text(
                        text = "Принять"
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageFields(
    images: List<Image>,
    component: ProfileEditorScreenComponent
) {
    val matrix = (0 until MAX_IMAGE).chunked(COLUMNS_COUNT)
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        matrix.onEach { line ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                line.onEach { i ->
                    val image = images.getOrNull(i)

                    val fieldModifier = Modifier.weight(1f).aspectRatio(ratio = 2.0f / 3)
                        .clip(RoundedCornerShape(10.dp))
                    if (image != null) {
                        ProfileImage(fieldModifier, image, component::onEvent)
                    } else {
                        ImageField(
                            modifier = fieldModifier.background(Color(color = 0xFFD9D9D9))
                                .innerShadow(
                                    color = Color(color = 0xFFA1A1A1),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                        ) {
                            component.onEvent(ImageAddEvent(it))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileImage(modifier: Modifier, image: Image, onEvent: (ProfileEditorEvent) -> Unit) {
    val context = LocalPlatformContext.current
    val imageLoader: ImageLoader = koinInject { parametersOf(context) }

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.clickable {
            showDialog = true
        }
    ) {
        AsyncImage(
            modifier = Modifier.background(Color.DarkGray),
            imageLoader = imageLoader,
            model = image.raw(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }

    if (showDialog) {
        EditImageDialog(
            delete = {
                onEvent(DeleteImageEvent(image))
            },
            onDismissRequest = {
                showDialog = false
            }
        )
    }
}

@Composable
private fun ImageField(
    modifier: Modifier = Modifier,
    onChange: (Image) -> Unit
) {
    val imagePicker = rememberImagePicker { uri ->
        uri?.let {
            onChange(it)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier.clickable {
            coroutineScope.launch {
                imagePicker.pickImage()
            }
        }
    ) {
        Icon(
            modifier = Modifier.size(50.dp).align(Alignment.Center),
            tint = CoreColors.background.mainColor,
            painter = CoreIcons.Editor.Camera,
            contentDescription = "Add camera",
        )
    }
}

@Composable
private fun TextEditField(
    text: String,
    label: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            modifier = Modifier.wrapContentSize().align(Alignment.Start)
        )
        TextInputField(
            text = text,
            hint = "",
            onChange = onChange,
            singleLine = false,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(125.dp).fillMaxWidth()
        )
    }
}

@Composable
private fun TopBar(onEvent: (ProfileEditorEvent) -> Unit) {
    Box(
        modifier = Modifier.background(CoreColors.background.mainColor)
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEvent(NavigateBackEvent) },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = CoreIcons.BackArrow,
                    contentDescription = "Back",
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = StaticTextId.UiId.Editor.translate(),
                fontSize = 30.sp,
                color = CoreColors.background.textColor,
                fontFamily = PlayfairDisplay
            )
        }
    }
}

private const val MAX_IMAGE = 6
private const val COLUMNS_COUNT = 3
