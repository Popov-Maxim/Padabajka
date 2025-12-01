package com.padabajka.dating.feature.profile.presentation.editor.asset

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.IconData
import com.padabajka.dating.core.presentation.ui.drawable.icon.toData
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.presentation.editor.model.LifestyleFields
import com.padabajka.dating.feature.profile.presentation.editor.model.LifestyleUIItem
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField
import kotlinx.collections.immutable.ImmutableList

@Composable
fun LifestyleBlock(
    field: ProfileField<LifestyleFields>,
    onEvent: (ProfileEditorEvent) -> Unit
) {
    val supportedLifestyles = field.value.supportedLifestyles
    var bottomSheetTabState: LifestyleTab? by remember { mutableStateOf(null) }
    Column {
        Smoking(supportedLifestyles.smoking.value) {
            bottomSheetTabState = LifestyleTab.Smoking
        }
        LifestyleDivider()
        Alcohol(supportedLifestyles.alcohol.value) {
            bottomSheetTabState = LifestyleTab.Alcohol
        }
        LifestyleDivider()
        Animals(supportedLifestyles.animals.value) {
            bottomSheetTabState = LifestyleTab.Animals
        }
    }

    bottomSheetTabState?.let { state ->
        LifestylesEditorBottomSheet(
            tabsState = state,
            supportedDetails = supportedLifestyles,
            onEvent = onEvent,
            onTabSelected = {
                bottomSheetTabState = it
            },
            onDismissRequest = {
                bottomSheetTabState = null
            }
        )
    }
}

@Composable
private fun LifestyleDivider() {
    Box(Modifier.padding(horizontal = 10.dp)) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black.copy(alpha = 0.25f)
        )
    }
}

@Composable
private fun Smoking(
    lifestyle: LifestyleUIItem,
    onClick: () -> Unit
) {
    LifestyleField(
        iconData = CoreIcons.Lifestyle.Smoking.toData(),
        title = StaticTextId.UiId.Smoking,
        value = lifestyle.value,
        description = lifestyle.attributes,
        defaultDescription = StaticTextId.UiId.SmokingHint.translate(),
        onClick = onClick
    )
}

@Composable
private fun Alcohol(
    lifestyle: LifestyleUIItem,
    onClick: () -> Unit
) {
    LifestyleField(
        iconData = CoreIcons.Lifestyle.Alcohol.toData(),
        title = StaticTextId.UiId.Alcohol,
        value = lifestyle.value,
        description = lifestyle.attributes,
        defaultDescription = StaticTextId.UiId.AlcoholHint.translate(),
        onClick = onClick
    )
}

@Composable
private fun Animals(
    lifestyle: LifestyleUIItem,
    onClick: () -> Unit
) {
    LifestyleField(
        iconData = CoreIcons.Lifestyle.Animals.toData(),
        title = StaticTextId.UiId.Animals,
        value = lifestyle.value,
        description = lifestyle.attributes,
        defaultDescription = StaticTextId.UiId.AnimalsHint.translate(),
        onClick = onClick
    )
}

@Composable
fun LifestyleField(
    iconData: IconData?,
    title: StaticTextId,
    value: Text?,
    description: ImmutableList<Text>,
    defaultDescription: String,
    onClick: () -> Unit
) {
    val mappingDescription = description.takeIf { it.isNotEmpty() }
        ?.map { it.translate() }
        ?.joinToString(separator = ", ")
    val description = mappingDescription
        ?: defaultDescription
    val descriptionColor = if (mappingDescription == null) {
        Color.Black.copy(alpha = 0.4f)
    } else {
        Color.Black.copy(alpha = 0.6f)
    }
    CoreLifestyleField(
        iconData = iconData,
        title = title.translate(),
        value = value?.translate(),
        description = description,
        descriptionColor = descriptionColor,
        onClick = onClick
    )
}

@Composable
private fun CoreLifestyleField(
    iconData: IconData?,
    title: String,
    value: String?,
    description: String,
    descriptionColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick).padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            iconData = iconData ?: IconData.Empty,
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val textTitle = if (value == null) "$title:" else "$title: $value"
            Text(
                text = textTitle,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
            Text(
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                color = descriptionColor
            )
        }
        Box(modifier = Modifier.size(24.dp))
    }
}
