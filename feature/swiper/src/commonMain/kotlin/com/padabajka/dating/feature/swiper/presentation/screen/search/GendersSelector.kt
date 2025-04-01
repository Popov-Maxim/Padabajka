package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.padabajka.dating.core.repository.api.model.profile.Gender

@Composable
fun GendersSelector(
    selectedGender: Gender,
    update: (Gender) -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow {
            val genders = Gender.entries
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
    gender: Gender,
    shape: Shape,
    selected: Boolean,
    select: (Gender) -> Unit,
    unselect: (Gender) -> Unit
) {
    SegmentedButton(
        onClick = {
            val newSelected = selected.not()
            if (newSelected) {
                select(gender)
            } else {
                unselect(gender)
            }
        },
        selected = selected,
        icon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier
                )
            }
        } else {
            {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Done icon",
                    modifier = Modifier
                )
            }
        },
        shape = shape,
        label = {
            Text(gender.raw)
        }
    )
}
