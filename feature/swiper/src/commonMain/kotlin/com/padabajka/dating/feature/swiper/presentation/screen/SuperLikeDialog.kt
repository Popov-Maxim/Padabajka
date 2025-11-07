package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CoreTextEditField
import com.padabajka.dating.core.presentation.ui.ProfileAvatar
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.repository.api.model.profile.raw
import com.padabajka.dating.feature.swiper.presentation.model.PersonItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperLikeDialog(
    cardItem: PersonItem,
    apply: (String) -> Unit,
    cancel: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(true)
    ModalBottomSheet(
        sheetState = bottomSheetState,
        containerColor = CoreColors.background.mainColor,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = {
            cancel()
            onDismissRequest()
        },
        dragHandle = null // TODO add dragHandle
    ) {
        DialogContent(
            cardItem = cardItem,
            apply = {
                apply(it)
                onDismissRequest()
            }
        )
    }
}

@Composable
private fun DialogContent(
    cardItem: PersonItem,
    apply: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp).padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ProfileAvatar(
            model = cardItem.images.firstOrNull()?.raw(),
            modifier = Modifier.size(120.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = StaticTextId.UiId.SuperLikeTitle.translate(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = StaticTextId.UiId.SuperLikeBody.translate()
                    .replace("{name}", cardItem.name),
                textAlign = TextAlign.Center,
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = StaticTextId.UiId.MessageTitle.translate(),
            )
            CoreTextEditField(
                singleLine = false,
                text = message,
                hint = StaticTextId.UiId.HintSuperLikeMessage.translate(),
                modifier = Modifier.height(125.dp).fillMaxWidth(),
                onChange = {
                    message = it
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = StaticTextId.UiId.SuperLikeCountTitle.translate(),
            )
            Text(
                text = "1 суперлайк",
            )
        }

        CoreCallToActionButton(
            modifier = Modifier.padding(horizontal = 13.dp),
            text = StaticTextId.UiId.Send.translate(),
            onClick = {
                apply(message)
            }
        )
    }
}
