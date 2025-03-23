package com.fp.padabajka.core.presentation.ui.font

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import padabajka.core.presentation.generated.resources.PlayfairDisplay_Italic_VariableFont_wght
import padabajka.core.presentation.generated.resources.PlayfairDisplay_VariableFont_wght
import padabajka.core.presentation.generated.resources.Res

val PlayfairDisplay
    @Composable
    get() = FontFamily(
        Font(
            resource = Res.font.PlayfairDisplay_VariableFont_wght,
            weight = FontWeight.Bold
        ),
        Font(
            resource = Res.font.PlayfairDisplay_VariableFont_wght,
            weight = FontWeight.Normal
        ),
        Font(
            resource = Res.font.PlayfairDisplay_Italic_VariableFont_wght,
            weight = FontWeight.Bold,
            style = FontStyle.Italic
        ),
    )
