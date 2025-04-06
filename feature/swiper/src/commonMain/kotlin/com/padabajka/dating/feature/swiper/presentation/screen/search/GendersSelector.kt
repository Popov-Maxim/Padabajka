package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.presentation.ui.toDpSize
import com.padabajka.dating.feature.swiper.presentation.model.GenderUI

@Composable
fun GendersSelector(
    selectedGender: GenderUI,
    update: (GenderUI) -> Unit
) {
    val genders = GenderUI.entries
    val selectedIndex = genders.indexOf(selectedGender)

    var containerInPixels by remember { mutableStateOf(IntSize(0, 0)) }
    val container = containerInPixels.toDpSize()
    val buttonSize = container.width / genders.size
    val xSelectedBoxPosition by animateDpAsState(
        targetValue = buttonSize * selectedIndex,
    )

    Box(
        Modifier.fillMaxWidth().height(40.dp).onGloballyPositioned {
            containerInPixels = it.size
        }.background(Color(color = 0x551B4564), RoundedCornerShape(20.dp))
    ) {
        if (container != DpSize.Zero) {
            Box(
                modifier = Modifier.width(buttonSize).height(container.height)
                    .offset(x = xSelectedBoxPosition)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            color = CoreColors.secondary.mainColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                )
            }
        }
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            genders.forEachIndexed { index, gender ->
                GenderSegment(
                    gender = gender,
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = genders.size),
                    selected = selectedGender == gender,
                    select = { selected -> update.invoke(selected) },
                    unselect = { unselected -> update.invoke(unselected) }
                )
            }
        }
    }
}

@Composable
private fun SingleChoiceSegmentedButtonRowScope.GenderSegment(
    gender: GenderUI,
    shape: Shape,
    selected: Boolean,
    select: (GenderUI) -> Unit,
    unselect: (GenderUI) -> Unit
) {
    SegmentedButton(
        colors = SegmentedButtonDefaults.colors(
            activeContainerColor = Color.Transparent,
            inactiveContainerColor = Color.Transparent,
            activeContentColor = CoreColors.secondary.textColor,
            inactiveContentColor = Color(color = 0xFF7C7C7C)
        ),
        border = SegmentedButtonDefaults.borderStroke(Color.Transparent, 0.dp),
        modifier = Modifier.weight(1f).fillMaxWidth(),
        onClick = {
            val newSelected = selected.not()
            if (newSelected) {
                select(gender)
            } else {
                unselect(gender)
            }
        },
        selected = selected,
        icon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier
                )
            }
        },
        shape = shape,
        label = {
            Text(
                text = gender.textId.translate(),
                fontSize = 15.sp
            )
        }
    )
}
