package com.padabajka.dating.feature.profile.presentation.editor.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditImageDialog(
    delete: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        tonalElevation = 0.dp,
        containerColor = Color.Transparent,
//        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        dragHandle = null // TODO add dragHandle
    ) {
        Button(
            onClick = {
                delete()
                onDismissRequest()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Delete")
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}
