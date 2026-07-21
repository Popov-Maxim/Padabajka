package com.padabajka.dating.feature.legal.presentation

import FormattedLegalText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.GhostButton
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.feature.legal.presentation.model.NewLegalAgreementsEvent
import defaultOnLinkClick
import parseLegalTextPattern

@Composable
fun NewLegalAgreementsScreen(
    component: NewLegalAgreementsComponent
) {
    val state by component.state.subscribeAsState()
    val onLinkClick: (String) -> Unit = defaultOnLinkClick(state.terms, state.privacy)
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 45.dp).padding(top = 100.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = StaticTextId.UiId.NewLegalTitle.translate(),
                fontFamily = PlayfairDisplay,
                fontSize = 26.sp,
                lineHeight = 1.33.em,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            val text = StaticTextId.UiId.NewLegalBody.translate()

            MainText(text, onLinkClick)
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 35.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            GhostButton(
                text = StaticTextId.UiId.LogOut.translate(),
                onClick = {
                    component.onEvent(NewLegalAgreementsEvent.Logout)
                }
            )

            CoreCallToActionButton(
                text = StaticTextId.UiId.ContinueBtn.translate(),
                onClick = {
                    component.onEvent(NewLegalAgreementsEvent.Apply)
                }
            )

            FormattedLegalText(
                textPattern = StaticTextId.UiId.NewLegalTextForAccept.translate(),
                onLinkClick = onLinkClick
            )
        }
    }
}

@Composable
fun MainText(
    text: String,
    onLinkClick: (String) -> Unit
) {
    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        ),
        pressedStyle = SpanStyle(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    )
    Text(
        text = parseLegalTextPattern(text, linkStyles, onLinkClick),
        fontFamily = PlayfairDisplay,
        fontSize = 20.sp,
        lineHeight = 1.33.em,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
    )
}
