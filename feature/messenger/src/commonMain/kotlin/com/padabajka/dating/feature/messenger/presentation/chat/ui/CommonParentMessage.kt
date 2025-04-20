package com.padabajka.dating.feature.messenger.presentation.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.ParentMessageItem

@Composable
fun CommonParentMessage(
    parentMessage: ParentMessageItem,
    colors: ParentMessageColors = ParentMessageColors.default(),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min)
            .run {
                colors.backgroundBrush?.let(::background) ?: this
            },
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Spacer(
            modifier = Modifier.fillMaxHeight().width(1.dp)
                .background(colors.messageHighlightColor),
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = colors.authorColor,
                        fontSize = 10.sp,
                    )
                ) {
                    append("Ответ:\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = colors.textColor,
                        fontSize = 13.sp,
                    )
                ) {
                    append(parentMessage.content)
                }
            },
            lineHeight = (13 * 1.2).sp,
            maxLines = 2
        )
    }
}

data class ParentMessageColors(
    val textColor: Color,
    val authorColor: Color,
    val messageHighlightColor: Color,
    val backgroundBrush: Brush? = null
) {
    companion object {
        fun default(): ParentMessageColors {
            return ParentMessageColors(
                textColor = Color.Unspecified,
                authorColor = CoreColors.Chat.secondary.mainColor,
                messageHighlightColor = CoreColors.Chat.secondary.mainColor
            )
        }
    }
}
