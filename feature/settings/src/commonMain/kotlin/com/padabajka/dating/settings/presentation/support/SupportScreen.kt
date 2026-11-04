package com.padabajka.dating.settings.presentation.support

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.domain.config.SUPPORT_EMAIL
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.CoreTextEditField
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.layout.SimpleTopBar
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.settings.presentation.model.support.SupportCategory
import com.padabajka.dating.settings.presentation.model.support.SupportEvent

@Composable
fun SupportScreen(component: SupportScreenComponent) {
    val state by component.state.subscribeAsState()

    CustomScaffold(
        topBar = {
            SimpleTopBar(
                title = StaticTextId.UiId.Support.translate(),
                navigateBack = { component.onEvent(SupportEvent.NavigateBack) }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 50.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(StaticTextId.UiId.SupportCategory.translate())
                CategorySelector(
                    category = state.category,
                    onSelect = { component.onEvent(SupportEvent.SelectCategory(it)) }
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(StaticTextId.UiId.SupportDescription.translate())
                CoreTextEditField(
                    text = state.description,
                    hint = StaticTextId.UiId.SupportDescriptionHint.translate(),
                    singleLine = false,
                    onChange = { component.onEvent(SupportEvent.UpdateDescription(it)) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 184.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CoreCallToActionButton(
                    text = StaticTextId.UiId.SupportOpenMail.translate(),
                    enabled = state.category != null && state.description.isNotBlank(),
                    isLoading = state.isOpeningMail,
                    onClick = { component.onEvent(SupportEvent.OpenMail) }
                )
                SupportHint(StaticTextId.UiId.SupportMailHint)
            }
            DirectContact()
        }
    }
}

@Composable
private fun CategorySelector(category: SupportCategory?, onSelect: (SupportCategory) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(20.dp)

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val menuWidth = maxWidth
        Row(
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 56.dp)
                .clip(shape)
                .background(Color.White)
                .innerShadow(shape = shape)
                .clickable(role = Role.Button) { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category?.title?.translate() ?: StaticTextId.UiId.SupportCategoryHint.translate(),
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = CoreColors.background.textColor.copy(alpha = if (category == null) 0.5f else 1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = CoreIcons.BackArrow,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
                    .rotate(degrees = if (expanded) 90f else -90f)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(menuWidth),
            shape = shape,
            offset = DpOffset(x = 0.dp, y = 5.dp),
            containerColor = Color.White,
            tonalElevation = 0.dp,
            shadowElevation = 5.dp,
            border = BorderStroke(1.dp, CoreColors.background.textColor.copy(alpha = 0.09f))
        ) {
            SupportCategory.entries.forEach { item ->
                val selected = item == category
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.title.translate(),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = if (selected) CoreColors.secondary.textColor else CoreColors.background.textColor
                        )
                    },
                    modifier = Modifier.padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (selected) CoreColors.secondary.mainColor else Color.White),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
@Suppress("DEPRECATION")
private fun DirectContact() {
    val clipboard = LocalClipboardManager.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SupportHint(StaticTextId.UiId.SupportDirectContact)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectionContainer(modifier = Modifier.weight(1f)) {
                Text(
                    text = SUPPORT_EMAIL,
                    color = CoreColors.secondary.mainColor
                )
            }
            TextButton(onClick = { clipboard.setText(AnnotatedString(SUPPORT_EMAIL)) }) {
                Text(StaticTextId.UiId.MessagePopupCopy.translate())
            }
        }
        SupportHint(StaticTextId.UiId.SupportDirectContactHint)
    }
}

@Composable
private fun SupportHint(text: StaticTextId.UiId) {
    Text(
        text = text.translate(),
        fontSize = 14.sp,
        color = CoreColors.background.textColor.copy(alpha = 0.6f)
    )
}
