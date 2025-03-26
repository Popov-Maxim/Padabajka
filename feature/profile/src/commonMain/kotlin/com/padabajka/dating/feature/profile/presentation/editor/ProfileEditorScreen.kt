package com.padabajka.dating.feature.profile.presentation.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.TextInputField
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.image.rememberImagePicker
import com.padabajka.dating.feature.profile.presentation.editor.model.AboutMeFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DiscardProfileUpdatesClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.FirstNameFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ImageAddEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LastNameFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SaveProfileUpdatesClickEvent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileEditorScreen(component: ProfileEditorScreenComponent) {
    val state by component.state.subscribeAsState()
    Column {
        val firstName = state.firstName.value
        val modifier = Modifier.height(70.dp).padding(10.dp)
        TextEditField(
            text = firstName,
            label = "First Name",
            onChange = {
                component.onEvent(FirstNameFieldUpdateEvent(it))
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(40.dp))

        val lastName = state.lastName.value
        TextEditField(
            text = lastName,
            label = "Last Name",
            onChange = {
                component.onEvent(LastNameFieldUpdateEvent(it))
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(40.dp))

        val aboutMe = state.aboutMe.value
        TextEditField(
            text = aboutMe,
            label = "About Me",
            onChange = {
                component.onEvent(AboutMeFieldUpdateEvent(it))
            },
            modifier = modifier
        )

        ImageFields(images = state.images.value, component = component)

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

@Composable
private fun ImageFields(
    images: List<Image>,
    component: ProfileEditorScreenComponent
) {
    val context = LocalPlatformContext.current
    val imageLoader: ImageLoader = koinInject { parametersOf(context) }

    val matrix = (0 until MAX_IMAGE).chunked(COLUMNS_COUNT)
    Column {
        matrix.onEach { line ->
            Row {
                line.onEach { i ->
                    val image = images.getOrNull(i)

                    val fieldModifier = Modifier.weight(1f).height(200.dp).padding(1.dp)
                    if (image != null) {
                        AsyncImage(
                            modifier = fieldModifier.background(Color.DarkGray),
                            imageLoader = imageLoader,
                            model = image.raw(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        ImageField(
                            modifier = fieldModifier.background(Color.DarkGray.copy(alpha = 0.5f)),
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
private fun ImageField(
    modifier: Modifier = Modifier,
    onChange: (Image) -> Unit
) {
    val imagePicker = rememberImagePicker { uri ->
        uri?.let {
            onChange(it)
        }
    }
    IconButton(
        onClick = {
            MainScope().launch {
                imagePicker.pickImage()
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Settings",
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
    Row(modifier) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Text(
                text = label,
                modifier = Modifier.wrapContentSize().align(Alignment.CenterStart)
            )
        }
        TextInputField(
            text = text,
            hint = "",
            onChange = onChange,
            modifier = Modifier.weight(2f).fillMaxHeight()
        )
    }
}

private const val MAX_IMAGE = 6
private const val COLUMNS_COUNT = 3
