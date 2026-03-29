package com.padabajka.dating.feature.profile.presentation.editor.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.GhostButton
import com.padabajka.dating.core.presentation.ui.SimpleConfirmDialog
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditImageDialog(
    replace: (() -> Unit)? = null,
    delete: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        tonalElevation = 0.dp,
        containerColor = Color.Transparent,
        scrimColor = Color.White.copy(alpha = 0.5f),
//        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        dragHandle = null // TODO add dragHandle
    ) {
        var showDeletingDialog: Boolean by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (replace != null) {
                EditImageButton(
                    text = StaticTextId.UiId.Replace.translate(),
                    onClick = {
                        replace()
                    },
                )
            }
            EditImageButton(
                text = StaticTextId.UiId.Delete.translate(),
                onClick = {
                    showDeletingDialog = true
                },
            )
            EditImageButton(
                text = StaticTextId.UiId.Cancel.translate(),
                onClick = {
                    onDismissRequest()
                },
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        if (showDeletingDialog) {
            SimpleConfirmDialog(
                text = StaticTextId.UiId.DeleteImageAlertDialogText.translate(),
                confirmText = StaticTextId.UiId.Yes.translate(),
                onConfirm = {
                    delete()
                    onDismissRequest()
                },
                dismissText = StaticTextId.UiId.No.translate(),
                onDismiss = { showDeletingDialog = false },
            )
        }
    }
}

@Composable
private fun EditImageButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)

    GhostButton(
        text = text,
        onClick = onClick,
        shape = shape,
        modifier = modifier.fillMaxWidth()
            .background(CoreColors.background.mainColor, shape)
    )
}
