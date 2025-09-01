package com.padabajka.dating.feature.profile.presentation.creator.gender.ui

import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.GenderUI

@Composable
fun GenderSelector(
    selectedGender: GenderUI?,
    select: (GenderUI) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        space = (-25).dp
    ) {
        GenderSegment(
            selectedGender = selectedGender,
            gender = GenderUI.Male,
            select = select
        )
        GenderSegment(
            selectedGender = selectedGender,
            gender = GenderUI.Female,
            select = select
        )
    }
}

@Composable
private fun SingleChoiceSegmentedButtonRowScope.GenderSegment(
    selectedGender: GenderUI?,
    gender: GenderUI,
    select: (GenderUI) -> Unit,
) {
    CoreSegmentedButton(
        selected = selectedGender == gender,
        onClick = { select(gender) },
        text = gender.textId
    )
}
